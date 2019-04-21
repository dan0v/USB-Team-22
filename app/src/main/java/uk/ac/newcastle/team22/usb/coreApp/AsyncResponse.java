package uk.ac.newcastle.team22.usb.coreApp;

public interface AsyncResponse {
    public void onComplete();

    public void onBadNetwork();
}