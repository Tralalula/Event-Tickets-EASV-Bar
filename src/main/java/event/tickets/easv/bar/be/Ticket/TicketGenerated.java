package event.tickets.easv.bar.be.Ticket;

public class TicketGenerated {

    private int id;
    private int eventId;
    private int customerId;
    private boolean assigned;
    private boolean used;
    String barcode;
    String qrcode;

    public TicketGenerated(int id, int eventId, int customerId,
                           boolean assigned, boolean used, String barcode, String qrcode)
    {
        this.id = id;
        this.eventId = eventId;
        this.customerId = customerId;
        this.assigned = assigned;
        this.used = used;
        this.barcode = barcode;
        this.qrcode = qrcode;
    }

    public TicketGenerated(int id, TicketGenerated ticketGenerated) {
        this(id, ticketGenerated.getEventId(), ticketGenerated.getCustomerId(), ticketGenerated.isAssigned(),
                ticketGenerated.isUsed(), ticketGenerated.getBarcode(), ticketGenerated.getQrcode());
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getEventId() {
        return eventId;
    }

    public void setEventId(int eventId) {
        this.eventId = eventId;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public boolean isAssigned() {
        return assigned;
    }

    public void setAssigned(boolean assigned) {
        this.assigned = assigned;
    }

    public boolean isUsed() {
        return used;
    }

    public void setUsed(boolean used) {
        this.used = used;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public String getQrcode() {
        return qrcode;
    }

    public void setQrcode(String qrcode) {
        this.qrcode = qrcode;
    }

    @Override
    public String toString() {
        return "TicketGenerated{" +
                "id=" + id +
                ", eventId=" + eventId +
                ", customerId=" + customerId +
                ", assigned=" + assigned +
                ", used=" + used +
                ", barcode='" + barcode + '\'' +
                ", qrcode='" + qrcode + '\'' +
                '}';
    }
}
