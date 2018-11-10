package entity;

import javax.persistence.*;

@Entity
@Table(name = "request_product", schema = "cosmos", catalog = "")
public class RequestProductEntity {
    private int id;
    private Integer count;
    private ClientRequestEntity clientRequestByRequestId;
    private ProductEntity productByProductId;

    @Id
    @Column(name = "id", nullable = false)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "count", nullable = true)
    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RequestProductEntity that = (RequestProductEntity) o;

        if (id != that.id) return false;
        if (count != null ? !count.equals(that.count) : that.count != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (count != null ? count.hashCode() : 0);
        return result;
    }

    @ManyToOne
    @JoinColumn(name = "request_id", referencedColumnName = "id")
    public ClientRequestEntity getClientRequestByRequestId() {
        return clientRequestByRequestId;
    }

    public void setClientRequestByRequestId(ClientRequestEntity clientRequestByRequestId) {
        this.clientRequestByRequestId = clientRequestByRequestId;
    }

    @ManyToOne
    @JoinColumn(name = "product_id", referencedColumnName = "id")
    public ProductEntity getProductByProductId() {
        return productByProductId;
    }

    public void setProductByProductId(ProductEntity productByProductId) {
        this.productByProductId = productByProductId;
    }
}
