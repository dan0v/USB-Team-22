package uk.ac.newcastle.team22.usb.activities;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.telephony.PhoneNumberUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import uk.ac.newcastle.team22.usb.R;
import uk.ac.newcastle.team22.usb.coreApp.AbstractCardData;
import uk.ac.newcastle.team22.usb.coreApp.AbstractViewHolder;
import uk.ac.newcastle.team22.usb.coreUSB.StaffMember;
import uk.ac.newcastle.team22.usb.coreUSB.USBManager;

/**
 * A class which manages the staff member activity.
 *
 * @author Alexander MacLeod
 * @version 1.0
 */
public class StaffMemberActivity extends USBActivity {

    /** The selected staff member. */
    private StaffMember staffMember;

    /** The recycler view containing details of the staff member. */
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staff_member);

        // Determine the staff member to display.
        String staffMemberIdentifier = getIntent().getStringExtra("identifier");
        for (StaffMember staffMember : USBManager.shared.getBuilding().getStaffMembers()) {
            if (staffMember.getIdentifier().equals(staffMemberIdentifier)) {
                this.staffMember = staffMember;
                break;
            }
        }

        // Set the title of the activity.
        setTitle(R.string.staffDetails);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Configure the recycler view.
        recyclerView = findViewById(R.id.staff_member_recycler_view);

        List<AbstractCardData> details = buildCards();
        StaffMemberAdapter adapter = new StaffMemberAdapter(details, this);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * @return The cards to display for the staff member.
     */
    private List<AbstractCardData> buildCards() {
        List<AbstractCardData> cards = new ArrayList();

        // Add a title card for the staff member.
        StaffMemberTitleCardData titleData = new StaffMemberTitleCardData(staffMember);
        cards.add(titleData);

        // Add the room of the staff member, if available.
        if (staffMember.getRoom() != null) {
            String roomAddress = staffMember.getRoom().getFormattedName(this);
            StaffMemberContactCardData roomData = new StaffMemberContactCardData(StaffMemberContactCardData.ContactOption.ROOM, roomAddress);
            cards.add(roomData);
        }

        // Add the email of the staff member, if available.
        if (staffMember.getEmailAddress() != null) {
            StaffMemberContactCardData emailData = new StaffMemberContactCardData(StaffMemberContactCardData.ContactOption.EMAIL, staffMember.getEmailAddress());
            cards.add(emailData);
        }

        // Add the phone of the staff member, if available.
        if (staffMember.getPhoneNumber() != null) {
            String formattedNumber = PhoneNumberUtils.formatNumber(staffMember.getPhoneNumber(), "GB");
            if (formattedNumber == null) {
                formattedNumber = staffMember.getPhoneNumber();
            }
            StaffMemberContactCardData phoneData = new StaffMemberContactCardData(StaffMemberContactCardData.ContactOption.PHONE, formattedNumber);
            cards.add(phoneData);
        }

        return cards;
    }
}

/**
 * A class which defines the detail views of a staff member.
 *
 * @author Alexander MacLeod
 * @version 1.0
 */
class StaffMemberAdapter extends RecyclerView.Adapter<AbstractViewHolder> {

    /** The list of cards. */
    private List<AbstractCardData> cardList;

    /** The activity which displays content in this adapter. */
    private Activity activity;

    public StaffMemberAdapter(List<AbstractCardData> cardList, Activity activity) {
        this.cardList = cardList;
        this.activity = activity;
    }

    public AbstractViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        AbstractViewHolder holder;
        View itemView;
        Context context = viewGroup.getContext();

        switch (viewType) {
            case 0:
                itemView = LayoutInflater.from(context).inflate(R.layout.card_view_staff_member_title, viewGroup, false);
                holder = new StaffMemberTitleViewHolder(itemView, context);
                break;
            default:
                itemView = LayoutInflater.from(context).inflate(R.layout.card_view_staff_member_contact, viewGroup, false);
                holder = new StaffMemberContactViewHolder(itemView);
                break;
        }

        return holder;
    }

    public void onBindViewHolder(AbstractViewHolder viewHolder, int position) {
        int viewType = getItemViewType(position);
        switch (viewType) {
            case 0: {
                StaffMemberTitleViewHolder updatingHolder = (StaffMemberTitleViewHolder) viewHolder;
                StaffMemberTitleCardData item = (StaffMemberTitleCardData) cardList.get(position);
                updatingHolder.nameTextView.setText(item.getFullName());
                break;
            }
            default: {
                StaffMemberContactViewHolder updatingHolder = (StaffMemberContactViewHolder) viewHolder;
                final StaffMemberContactCardData item = (StaffMemberContactCardData) cardList.get(position);
                updatingHolder.contactAddressTextView.setText(item.getAddress());
                updatingHolder.iconView.setImageResource(item.getOption().getImageRepresentation());

                viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = item.getOption().open(item.getAddress());
                        if (intent != null) {
                            if (item.getOption() == StaffMemberContactCardData.ContactOption.PHONE) {
                                startCallActivity(intent, activity, v.getContext());
                            } else {
                                v.getContext().startActivity(intent);
                            }
                        }
                    }
                });
                break;
            }
        }
    }

    public int getItemViewType(int position) {
        AbstractCardData card = cardList.get(position);
        if (card instanceof StaffMemberTitleCardData) {
            return 0;
        } else {
            return 1;
        }
    }

    public int getItemCount() {
        return cardList.size();
    }

    /**
     * Attempts to start the call activity.
     * Permission may be needed to allow initiating a new phone call.
     *
     * @param intent The call intent.
     * @param activity The activity.
     * @param context The context.
     */
    private void startCallActivity(Intent intent, Activity activity, Context context) {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, new String[]{ Manifest.permission.CALL_PHONE }, 0);
            return;
        }
        context.startActivity(intent);
    }
}

/**
 * A class which defines the data to be displayed by a {@link StaffMemberTitleViewHolder}.
 *
 * @author Alexander MacLeod
 * @version 1.0
 */
class StaffMemberTitleCardData extends AbstractCardData {

    /** The full name of the staff member. */
    private String fullName;

    StaffMemberTitleCardData(StaffMember staffMember) {
        this.fullName = staffMember.getFullTitle();
    }

    /**
     * @return The full name of the staff member.
     */
    public String getFullName() {
        return fullName;
    }
}

/**
 * A class which defines the view to be display data from a {@link StaffMemberTitleCardData}.
 *
 * @author Alexander MacLeod
 * @version 1.0
 */
class StaffMemberTitleViewHolder extends AbstractViewHolder {

    /** The text view which displays the full name of the staff member. */
    TextView nameTextView;

    /**
     * The image view which displays the profile picture of the staff member.
     * This will usually be set to a blank profile picture.
     */
    ImageView profilePicture;

    StaffMemberTitleViewHolder(View view, Context context) {
        super(view);
        nameTextView = view.findViewById(R.id.staffMemberTitleTextView);
        profilePicture = view.findViewById(R.id.staffMemberProfilePictureImageView);

        Bitmap avatar = BitmapFactory.decodeResource(context.getResources(), R.drawable.default_profile_picture);
        RoundedBitmapDrawable roundDrawable = RoundedBitmapDrawableFactory.create(context.getResources(), avatar);
        roundDrawable.setCircular(true);
        profilePicture.setImageDrawable(roundDrawable);
    }
}

/**
 * A class which defines the data to be displayed by a {@link StaffMemberContactViewHolder}.
 *
 * @author Alexander MacLeod
 * @version 1.0
 */
class StaffMemberContactCardData extends AbstractCardData {

    /** The address of the contact option. */
    private ContactOption option;

    /** The address of the contact option. */
    private String address;

    /** The type of staff member contact. */
    enum ContactOption {
        EMAIL, PHONE, ROOM;

        /**
         * Opens the contact option.
         *
         * @param address The address of the contact option.
         * @return The intent to open.
         */
        Intent open(String address) {
            Intent intent;
            switch (this) {
                case EMAIL:
                    intent = new Intent(Intent.ACTION_SEND);
                    intent.setType("message/rfc822");
                    intent.putExtra(Intent.EXTRA_EMAIL, new String[]{ address });
                    return intent;
                case PHONE:
                    intent = new Intent(Intent.ACTION_CALL);
                    intent.setData(Uri.parse("tel:" + address));
                    return intent;
                default:
                    return null;
            }
        }

        /**
         * @return Image representation of the contact option..
         */
        @DrawableRes
        int getImageRepresentation() {
            switch (this) {
                case EMAIL:
                    return R.drawable.email;
                case PHONE:
                    return R.drawable.phone;
                case ROOM:
                    return R.drawable.room;
                default:
                    return 0;
            }
        }
    }

    StaffMemberContactCardData(ContactOption option, String address) {
        this.option = option;
        this.address = address;
    }

    /**
     * @return The address of the contact option.
     */
    public ContactOption getOption() {
        return option;
    }

    /**
     * @return The address of the contact option.
     */
    public String getAddress() {
        return address;
    }
}

/**
 * A class which defines the view to be display data from a {@link StaffMemberContactCardData}.
 *
 * @author Alexander MacLeod
 * @version 1.0
 */
class StaffMemberContactViewHolder extends AbstractViewHolder {

    /** The text view which displays the address of the contact option. */
    TextView contactAddressTextView;

    /** The image view which displays an icon of the contact option. */
    ImageView iconView;

    StaffMemberContactViewHolder(View view) {
        super(view);
        contactAddressTextView = view.findViewById(R.id.staffMemberContactTextView);
        iconView = view.findViewById(R.id.staffMemberContactImageView);
    }
}
