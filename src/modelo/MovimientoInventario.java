package modelo;
import java.sql.Timestamp;

public class MovimientoInventario {
    private int id;
    private int productId;
    private String movementType; // "IN" o "OUT"
    private int quantity;
    private String note;
    private Timestamp date;
    private Integer userId;
    private String reference;

    // getters y setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getProductId() { return productId; }
    public void setProductId(int productId) { this.productId = productId; }
    public String getMovementType() { return movementType; }
    public void setMovementType(String movementType) { this.movementType = movementType; }
    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
    public String getNote() { return note; }
    public void setNote(String note) { this.note = note; }
    public Timestamp getDate() { return date; }
    public void setDate(Timestamp date) { this.date = date; }
    public Integer getUserId() { return userId; }
    public void setUserId(Integer userId) { this.userId = userId; }
    public String getReference() { return reference; }
    public void setReference(String reference) { this.reference = reference; }
}
