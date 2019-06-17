package cn.reve.pojo.order;

import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Table(name="tb_count_trade")
public class CountTrade implements Serializable {

    @Id
    private Integer id;
    private Integer personsAccessed;
    private Integer personsOrdered;
    private Integer orderNum;
    private Integer validOrderNum;
    private Integer turnover;
    private Integer refund;
    private Integer personsPayed;
    private Integer ordersPayed;
    private Integer sumOrdersPayed;
    private Integer sumMoney;
    private Integer turnoverPerCustomer;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getPersonsAccessed() {
        return personsAccessed;
    }

    public void setPersonsAccessed(Integer personsAccessed) {
        this.personsAccessed = personsAccessed;
    }

    public Integer getPersonsOrdered() {
        return personsOrdered;
    }

    public void setPersonsOrdered(Integer personsOrdered) {
        this.personsOrdered = personsOrdered;
    }

    public Integer getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(Integer orderNum) {
        this.orderNum = orderNum;
    }

    public Integer getValidOrderNum() {
        return validOrderNum;
    }

    public void setValidOrderNum(Integer validOrderNum) {
        this.validOrderNum = validOrderNum;
    }

    public Integer getTurnover() {
        return turnover;
    }

    public void setTurnover(Integer turnover) {
        this.turnover = turnover;
    }

    public Integer getRefund() {
        return refund;
    }

    public void setRefund(Integer refund) {
        this.refund = refund;
    }

    public Integer getPersonsPayed() {
        return personsPayed;
    }

    public void setPersonsPayed(Integer personsPayed) {
        this.personsPayed = personsPayed;
    }

    public Integer getOrdersPayed() {
        return ordersPayed;
    }

    public void setOrdersPayed(Integer ordersPayed) {
        this.ordersPayed = ordersPayed;
    }

    public Integer getSumOrdersPayed() {
        return sumOrdersPayed;
    }

    public void setSumOrdersPayed(Integer sumOrdersPayed) {
        this.sumOrdersPayed = sumOrdersPayed;
    }

    public Integer getSumMoney() {
        return sumMoney;
    }

    public void setSumMoney(Integer sumMoney) {
        this.sumMoney = sumMoney;
    }

    public Integer getTurnoverPerCustomer() {
        return turnoverPerCustomer;
    }

    public void setTurnoverPerCustomer(Integer turnoverPerCustomer) {
        this.turnoverPerCustomer = turnoverPerCustomer;
    }

    @Override
    public String toString() {
        return "CountTrade{" +
                "id=" + id +
                ", personsAccessed=" + personsAccessed +
                ", personsOrdered=" + personsOrdered +
                ", orderNum=" + orderNum +
                ", validOrderNum=" + validOrderNum +
                ", turnover=" + turnover +
                ", refund=" + refund +
                ", personsPayed=" + personsPayed +
                ", ordersPayed=" + ordersPayed +
                ", sumOrdersPayed=" + sumOrdersPayed +
                ", sumMoney=" + sumMoney +
                ", turnoverPerCustomer=" + turnoverPerCustomer +
                '}';
    }
}
