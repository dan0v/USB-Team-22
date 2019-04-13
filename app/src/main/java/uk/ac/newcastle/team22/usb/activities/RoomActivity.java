package uk.ac.newcastle.team22.usb.activities;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import uk.ac.newcastle.team22.usb.coreUSB.Floor;
import uk.ac.newcastle.team22.usb.coreUSB.Room;
import uk.ac.newcastle.team22.usb.coreUSB.USBManager;

/**
 * A class which manages the room activity.
 *
 * @author Alexander MacLeod
 * @version 1.0
 */
public class RoomActivity extends USBActivity {

    /** The selected room. */
    private Room room;

    /** The recycler view containing details of the staff member. */
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room);

        // Determine the room to display.
        int floorNumber = getIntent().getIntExtra("floorNumber", 0);
        String roomNumber = getIntent().getStringExtra("roomNumber");

        Floor floor = USBManager.shared.getBuilding().getFloors().get(floorNumber);
        for (Room room : floor.getRooms().values()) {
            if (room.getNumber().equals(roomNumber)) {
                this.room = room;
                break;
            }
        }

        // Set the title of the activity.
        setTitle(R.string.roomDetails);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Configure the recycler view.
        recyclerView = findViewById(R.id.room_recycler_view);

        List<AbstractCardData> details = buildCards();
        RoomAdapter adapter = new RoomAdapter(details, this);

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
     * @return The cards to display for the room.
     */
    private List<AbstractCardData> buildCards() {
        List<AbstractCardData> cards = new ArrayList();

        // Add a title card for the room.
        RoomTitleCardData titleData = new RoomTitleCardData(room);
        cards.add(titleData);

        return cards;
    }
}

/**
 * A class which defines the detail views of a room.
 *
 * @author Alexander MacLeod
 * @version 1.0
 */
class RoomAdapter extends RecyclerView.Adapter<AbstractViewHolder> {

    /** The list of cards. */
    private List<AbstractCardData> cardList;

    /** The activity which displays content in this adapter. */
    private Activity activity;

    public RoomAdapter(List<AbstractCardData> cardList, Activity activity) {
        this.cardList = cardList;
        this.activity = activity;
    }

    public AbstractViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        AbstractViewHolder holder;
        View itemView;
        Context context = viewGroup.getContext();

        switch (viewType) {
            case 0:
                itemView = LayoutInflater.from(context).inflate(R.layout.card_view_room_title, viewGroup, false);
                holder = new RoomTitleViewHolder(itemView);
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
                RoomTitleViewHolder updatingHolder = (RoomTitleViewHolder) viewHolder;
                RoomTitleCardData item = (RoomTitleCardData) cardList.get(position);
                String roomTitle;
                if (item.getRoom().getAlternateName() == null) {
                    roomTitle = item.getRoom().getFormattedName(activity);
                } else {
                    roomTitle = item.getRoom().getAlternateName();
                }
                updatingHolder.titleTextView.setText(roomTitle);
                updatingHolder.detailTextView.setText(item.getRoom().getFormattedNumber());
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
        if (card instanceof RoomTitleCardData) {
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
 * A class which defines the data to be displayed by a {@link RoomTitleViewHolder}.
 *
 * @author Alexander MacLeod
 * @version 1.0
 */
class RoomTitleCardData extends AbstractCardData {

    /** The room being displayed. */
    private Room room;

    RoomTitleCardData(Room room) {
        this.room = room;
    }

    /**
     * @return The room being displayed.
     */
    public Room getRoom() {
        return room;
    }
}

/**
 * A class which defines the view to be display data from a {@link RoomTitleCardData}.
 *
 * @author Alexander MacLeod
 * @version 1.0
 */
class RoomTitleViewHolder extends AbstractViewHolder {

    /** The text view which displays the room name. */
    TextView titleTextView;

    /** The text view which displays the room number. */
    TextView detailTextView;

    RoomTitleViewHolder(View view) {
        super(view);
        titleTextView = view.findViewById(R.id.roomTitleTextView);
        detailTextView = view.findViewById(R.id.roomDetailTextView);
    }
}

/**
 * A class which defines the data to be displayed by a {@link RoomResourceViewHolder}.
 *
 * @author Alexander MacLeod
 * @version 1.0
 */
class RoomResourceCardData extends AbstractCardData {

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

    RoomResourceCardData(ContactOption option, String address) {
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
 * A class which defines the view to be display data from a {@link RoomResourceCardData}.
 *
 * @author Alexander MacLeod
 * @version 1.0
 */
class RoomResourceViewHolder extends AbstractViewHolder {

    /** The text view which displays the address of the contact option. */
    TextView contactAddressTextView;

    /** The image view which displays an icon of the contact option. */
    ImageView iconView;

    RoomResourceViewHolder(View view) {
        super(view);
        contactAddressTextView = view.findViewById(R.id.staffMemberContactTextView);
        iconView = view.findViewById(R.id.staffMemberContactImageView);
    }
}
