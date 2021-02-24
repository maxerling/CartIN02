package Objects;

import java.util.Date;

/**
 * Created by Max Erling
 * Date: 2021-02-19
 * Copyright: MIT
 * Class: Java20B
 */
public class OutOfStock {
    private int id;
    private int productid;
    private Date date;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getProductid() {
        return productid;
    }

    public void setProductid(int productid) {
        this.productid = productid;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
