package schauweg.timetolive.config;

public class TTLConfig {

    private boolean overlayActive = true;
    private boolean displayInTicks = false;

    public boolean isOverlayActive() {
        return overlayActive;
    }

    public void setOverlayActive(boolean overlayActive) {
        this.overlayActive = overlayActive;
    }

    public boolean isDisplayInTicks() {
        return displayInTicks;
    }

    public void setDisplayInTicks(boolean displayInTicks) {
        this.displayInTicks = displayInTicks;
    }
}
