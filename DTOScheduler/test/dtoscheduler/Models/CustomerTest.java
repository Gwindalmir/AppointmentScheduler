/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dtoscheduler.Models;

import dtoscheduler.model.Customer;
import dtoscheduler.model.Address;
import dtoscheduler.singleton.GlobalInformation;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Daniel
 */
public class CustomerTest extends TestBase
{
    private Address address;

    public CustomerTest()
    {
    }

    @Override
    public void setUp() throws Exception
    {
        address = setUpAddress(setUpCity(setUpCountry()));
        setUpCustomer(address);
    }

    /**
     * Test of getCustomerId method, of class Customer.
     */
    @Test
    public void testGetCustomerId()
    {
        System.out.println("getCustomerId");
        Customer instance = new Customer();
        int expResult = 0;
        int result = instance.getCustomerId();
        assertEquals(expResult, result);
    }

    /**
     * Test of getCustomerName method, of class Customer.
     */
    @Test
    public void testGetCustomerName()
    {
        System.out.println("getCustomerName");
        Customer instance = new Customer();
        String expResult = "";
        String result = instance.getCustomerName();
        assertEquals(expResult, result);
    }

    /**
     * Test of setCustomerName method, of class Customer.
     */
    @Test
    public void testSetCustomerName()
    {
        System.out.println("setCustomerName");
        String customerName = "Robert Picard";
        Customer instance = new Customer();
        instance.setCustomerName(customerName);
        String result = instance.getCustomerName();
        assertEquals(customerName, result);
    }

    /**
     * Test of getActive method, of class Customer.
     */
    @Test
    public void testGetActive()
    {
        System.out.println("getActive");
        Customer instance = new Customer();
        boolean expResult = true;
        boolean result = instance.getActive();
        assertEquals(expResult, result);
    }

    /**
     * Test of setActive method, of class Customer.
     */
    @Test
    public void testSetActive()
    {
        System.out.println("setActive");
        boolean active = false;
        Customer instance = new Customer();
        instance.setActive(active);
        boolean result = instance.getActive();
        assertEquals(active, result);
    }

    /**
     * Test of getCreateDate method, of class Customer.
     */
    @Test
    public void testGetCreateDate()
    {
        System.out.println("getCreateDate");
        Customer instance = new Customer();
        ZonedDateTime expResult = ZonedDateTime.now().truncatedTo(ChronoUnit.SECONDS);
        ZonedDateTime result = instance.getCreateDate().truncatedTo(ChronoUnit.SECONDS);
        assertEquals(expResult, result);
    }

    /**
     * Test of setCreateDate method, of class Customer.
     */
    @Test
    public void testSetCreateDate()
    {
        System.out.println("setCreateDate");
        ZonedDateTime createDate = ZonedDateTime.now().truncatedTo(ChronoUnit.SECONDS);
        Customer instance = new Customer();
        instance.setCreateDate(createDate);
        ZonedDateTime result = instance.getCreateDate().truncatedTo(ChronoUnit.SECONDS);
        assertEquals(createDate, result);
    }

    /**
     * Test of getCreatedBy method, of class Customer.
     */
    @Test
    public void testGetCreatedBy()
    {
        System.out.println("getCreatedBy");
        Customer instance = new Customer();
        String expResult = GlobalInformation.getInstance().getUser();
        String result = instance.getCreatedBy();
        assertEquals(expResult, result);
    }

    /**
     * Test of setCreatedBy method, of class Customer.
     */
    @Test
    public void testSetCreatedBy()
    {
        System.out.println("setCreatedBy");
        String createdBy = "foo";
        Customer instance = new Customer();
        instance.setCreatedBy(createdBy);
        String result = instance.getCreatedBy();
        assertEquals(createdBy, result);
    }

    /**
     * Test of getLastUpdate method, of class Customer.
     */
    @Test
    public void testGetLastUpdate()
    {
        System.out.println("getLastUpdate");
        Customer instance = new Customer();
        ZonedDateTime expResult = ZonedDateTime.now().truncatedTo(ChronoUnit.SECONDS);
        ZonedDateTime result = instance.getLastUpdate().truncatedTo(ChronoUnit.SECONDS);
        assertEquals(expResult, result);
    }

    /**
     * Test of setLastUpdate method, of class Customer.
     */
    @Test
    public void testSetLastUpdate()
    {
        System.out.println("setLastUpdate");
        ZonedDateTime lastUpdate = ZonedDateTime.now().truncatedTo(ChronoUnit.SECONDS);
        Customer instance = new Customer();
        instance.setLastUpdate(lastUpdate);
        ZonedDateTime result = instance.getCreateDate().truncatedTo(ChronoUnit.SECONDS);
        assertEquals(lastUpdate, result);
    }

    /**
     * Test of getLastUpdateBy method, of class Customer.
     */
    @Test
    public void testGetLastUpdateBy()
    {
        System.out.println("getLastUpdateBy");
        Customer instance = new Customer();
        String expResult = GlobalInformation.getInstance().getUser();
        String result = instance.getLastUpdateBy();
        assertEquals(expResult, result);
    }

    /**
     * Test of setLastUpdateBy method, of class Customer.
     */
    @Test
    public void testSetLastUpdateBy()
    {
        System.out.println("setLastUpdateBy");
        String lastUpdateBy = "foo";
        Customer instance = new Customer();
        instance.setLastUpdateBy(lastUpdateBy);
        String result = instance.getLastUpdateBy();
        assertEquals(lastUpdateBy, result);
    }

    /**
     * Test of getAddressId method, of class Customer.
     */
    @Test
    public void testGetAddressId()
    {
        System.out.println("getAddressId");
        Customer instance = new Customer();
        Address expResult = null;
        Address result = instance.getAddressId();
        assertEquals(expResult, result);
    }

    /**
     * Test of setAddressId method, of class Customer.
     */
    @Test
    public void testSetAddressId()
    {
        System.out.println("setAddressId");
        Address addressId = new Address();
        Customer instance = new Customer();
        instance.setAddressId(addressId);
        Address result = instance.getAddressId();
        assertEquals(addressId, result);
    }

    /**
     * Test of update method, of class Customer.
     */
    @Test
    public void testUpdate() throws Exception
    {
        System.out.println("update");
        Customer instance = new Customer();
        String name = "Robert Picard";
        instance.setCustomerName("Jean Luc Picard");
        instance.refresh();
        instance.setCustomerName(name);
        instance.update();
        String result = instance.getCustomerName();
        assertEquals(name, result);
    }

    /**
     * Test of add method, of class Customer.
     */
    @Test
    public void testAdd() throws Exception
    {
        System.out.println("add");
        Customer instance = new Customer();
        instance.setCustomerName("Robert Picard");
        instance.setAddressId(address);
        instance.add();

        int result = instance.getCustomerId();
        assertNotEquals(0, result);

    }

    /**
     * Test of delete method, of class Customer.
     */
    @Test
    public void testDelete() throws Exception
    {
        System.out.println("delete");
        Customer instance = new Customer();
        instance.setCustomerName("Jean Luc Picard");
        instance.refresh();
        instance.delete();
    }

    /**
     * Test of refresh method, of class Customer.
     */
    @Test
    public void testRefresh() throws Exception
    {
        System.out.println("refresh");
        Customer instance = new Customer();
        instance.setCustomerName("Jean Luc Picard");
        instance.refresh();

        int result = instance.getCustomerId();
        assertNotEquals(0, result);
    }
}
