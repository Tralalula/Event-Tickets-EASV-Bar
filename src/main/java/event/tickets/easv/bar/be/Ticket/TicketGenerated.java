package event.tickets.easv.bar.be.Ticket;

import event.tickets.easv.bar.be.Entity;

import java.util.List;

public class TicketGenerated implements Entity<TicketGenerated> {

    private int id;
    private int eventId;
    private int customerId;
    private boolean assigned;
    private boolean used;
    String barcode;
    String qrcode;

    private List<TicketEvent> ticketEvents;

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

    public TicketGenerated(int eventId, int customerId) {
        this.eventId = eventId;
        this.customerId = customerId;
        this.assigned = false;
        this.used = false;
        this.barcode = "ASD";
        this.qrcode = "ASD";
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

    @Override
    public void update(TicketGenerated updatedData) {
        throw new UnsupportedOperationException("Er ikke implementeret. TicketGenerated.update()");
    }

    @Override
    public int id() {
        return this.id;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void setAssociations(List<?> associations) {
        if (associations.isEmpty()) return;

        Object first = associations.getFirst();
        if (first instanceof TicketEvent) {
            this.ticketEvents = (List<TicketEvent>) associations;
        }
    }
}
