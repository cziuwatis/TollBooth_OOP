package DTO;

import java.util.Objects;

/**
 *
 * @author Andrej Gorochov D00218937
 */
final public class Customer
{

    private final int id;
    private final String name;
    private final String address;
    private final double billAmount;

    private Customer()
    {
        id = 0;
        name = "";
        address = "";
        billAmount = 0;
    }

    private Customer(int id, String name, String address, double billAmount)
    {
        this.id = id;
        this.name = name;
        this.address = address;
        this.billAmount = billAmount;
    }

    private Customer(Customer c)
    {
        this.id = c.getId();
        this.name = c.getName();
        this.address = c.getAddress();
        this.billAmount = c.getBillAmount();
    }

    public int getId()
    {
        return id;
    }

    public String getName()
    {
        return name;
    }

    public String getAddress()
    {
        return address;
    }

    public double getBillAmount()
    {
        return billAmount;
    }

    @Override
    public int hashCode()
    {
        int hash = 7;
        hash = 29 * hash + this.id;
        hash = 29 * hash + Objects.hashCode(this.name);
        hash = 29 * hash + Objects.hashCode(this.address);
        hash = 29 * hash + (int) (Double.doubleToLongBits(this.billAmount) ^ (Double.doubleToLongBits(this.billAmount) >>> 32));
        return hash;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
        {
            return true;
        }
        if (obj == null)
        {
            return false;
        }
        if (getClass() != obj.getClass())
        {
            return false;
        }
        final Customer other = (Customer) obj;
        if (this.id != other.id)
        {
            return false;
        }
        if (Double.doubleToLongBits(this.billAmount) != Double.doubleToLongBits(other.billAmount))
        {
            return false;
        }
        if (!Objects.equals(this.name, other.name))
        {
            return false;
        }
        if (!Objects.equals(this.address, other.address))
        {
            return false;
        }
        return true;
    }

    @Override
    public String toString()
    {
        return "Customer{" + "id=" + id + ", name=" + name + ", address=" + address + ", billAmount=" + billAmount + '}';
    }

    public static Customer createCustomer(int id, String name, String address, double billAmount)
    {
        return new Customer(id, name, address, billAmount);
    }

    public static Customer createCustomer(Customer c)
    {
        return new Customer(c);
    }

}
