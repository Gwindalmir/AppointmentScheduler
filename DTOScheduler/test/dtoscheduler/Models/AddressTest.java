/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dtoscheduler.Models;

import dtoscheduler.model.Address;
import dtoscheduler.model.City;
import dtoscheduler.singleton.GlobalInformation;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Daniel
 */
public class AddressTest extends TestBase
{
    private City city;

    public AddressTest()
    {
    }

    @Override
    public void setUp() throws Exception
    {
        city = setUpCity(setUpCountry());
        setUpAddress(city);
    }

    /**
     * Test of getAddressId method, of class Address.
     */
    @Test
    public void testGetAddressId()
    {
        System.out.println("getAddressId");
        Address instance = new Address();
        int expResult = 0;
        int result = instance.getAddressId();
        assertEquals(expResult, result);
    }

    /**
     * Test of getAddress method, of class Address.
     */
    @Test
    public void testGetAddress()
    {
        System.out.println("getAddress");
        Address instance = new Address();
        String expResult = "";
        String result = instance.getAddress();
        assertEquals(expResult, result);
    }

    /**
     * Test of setAddress method, of class Address.
     */
    @Test
    public void testSetAddress()
    {
        System.out.println("setAddress");
        String address = "1701 Enterprise Way";
        Address instance = new Address();
        instance.setAddress(address);
        String result = instance.getAddress();
        assertEquals(address, result);
    }

    /**
     * Test of getAddress2 method, of class Address.
     */
    @Test
    public void testGetAddress2()
    {
        System.out.println("getAddress2");
        Address instance = new Address();
        String expResult = "";
        String result = instance.getAddress2();
        assertEquals(expResult, result);
    }

    /**
     * Test of setAddress2 method, of class Address.
     */
    @Test
    public void testSetAddress2()
    {
        System.out.println("setAddress2");
        String address2 = "Deck 10";
        Address instance = new Address();
        instance.setAddress2(address2);
        String result = instance.getAddress2();
        assertEquals(address2, result);
    }

    /**
     * Test of getPostalCode method, of class Address.
     */
    @Test
    public void testGetPostalCode()
    {
        System.out.println("getPostalCode");
        Address instance = new Address();
        String expResult = "";
        String result = instance.getPostalCode();
        assertEquals(expResult, result);
    }

    /**
     * Test of setPostalCode method, of class Address.
     */
    @Test
    public void testSetPostalCode()
    {
        System.out.println("setPostalCode");
        String postalCode = "90210";
        Address instance = new Address();
        instance.setPostalCode(postalCode);
        String result = instance.getPostalCode();
        assertEquals(postalCode, result);
    }

    /**
     * Test of getPhone method, of class Address.
     */
    @Test
    public void testGetPhone()
    {
        System.out.println("getPhone");
        Address instance = new Address();
        String expResult = "";
        String result = instance.getPhone();
        assertEquals(expResult, result);
    }

    /**
     * Test of setPhone method, of class Address.
     */
    @Test
    public void testSetPhone()
    {
        System.out.println("setPhone");
        String phone = "916-555-1234";
        Address instance = new Address();
        instance.setPhone(phone);
        String result = instance.getPhone();
        assertEquals(phone, result);
    }

    /**
     * Test of getCreateDate method, of class Address.
     */
    @Test
    public void testGetCreateDate()
    {
        System.out.println("getCreateDate");
        Address instance = new Address();
        ZonedDateTime expResult = ZonedDateTime.now().truncatedTo(ChronoUnit.SECONDS);
        ZonedDateTime result = instance.getCreateDate().truncatedTo(ChronoUnit.SECONDS);
        assertEquals(expResult, result);
    }

    /**
     * Test of setCreateDate method, of class Address.
     */
    @Test
    public void testSetCreateDate()
    {
        System.out.println("setCreateDate");
        ZonedDateTime createDate = ZonedDateTime.now().truncatedTo(ChronoUnit.SECONDS);
        Address instance = new Address();
        instance.setCreateDate(createDate);
        ZonedDateTime result = instance.getCreateDate().truncatedTo(ChronoUnit.SECONDS);
        assertEquals(createDate, result);
    }

    /**
     * Test of getCreatedBy method, of class Address.
     */
    @Test
    public void testGetCreatedBy()
    {
        System.out.println("getCreatedBy");
        Address instance = new Address();
        String expResult = GlobalInformation.getInstance().getUser();
        String result = instance.getCreatedBy();
        assertEquals(expResult, result);
    }

    /**
     * Test of setCreatedBy method, of class Address.
     */
    @Test
    public void testSetCreatedBy()
    {
        System.out.println("setCreatedBy");
        String createdBy = "foo";
        Address instance = new Address();
        instance.setCreatedBy(createdBy);
        String result = instance.getCreatedBy();
        assertEquals(createdBy, result);
    }

    /**
     * Test of getLastUpdate method, of class Address.
     */
    @Test
    public void testGetLastUpdate()
    {
        System.out.println("getLastUpdate");
        Address instance = new Address();
        ZonedDateTime expResult = ZonedDateTime.now().truncatedTo(ChronoUnit.SECONDS);
        ZonedDateTime result = instance.getLastUpdate().truncatedTo(ChronoUnit.SECONDS);
        assertEquals(expResult, result);
    }

    /**
     * Test of setLastUpdate method, of class Address.
     */
    @Test
    public void testSetLastUpdate()
    {
        System.out.println("setLastUpdate");
        ZonedDateTime lastUpdate = ZonedDateTime.now().truncatedTo(ChronoUnit.SECONDS);
        Address instance = new Address();
        instance.setLastUpdate(lastUpdate);
        ZonedDateTime result = instance.getCreateDate().truncatedTo(ChronoUnit.SECONDS);
        assertEquals(lastUpdate, result);
    }

    /**
     * Test of getLastUpdateBy method, of class Address.
     */
    @Test
    public void testGetLastUpdateBy()
    {
        System.out.println("getLastUpdateBy");
        Address instance = new Address();
        String expResult = GlobalInformation.getInstance().getUser();
        String result = instance.getLastUpdateBy();
        assertEquals(expResult, result);
    }

    /**
     * Test of setLastUpdateBy method, of class Address.
     */
    @Test
    public void testSetLastUpdateBy()
    {
        System.out.println("setLastUpdateBy");
        String lastUpdateBy = "foo";
        Address instance = new Address();
        instance.setLastUpdateBy(lastUpdateBy);
        String result = instance.getLastUpdateBy();
        assertEquals(lastUpdateBy, result);
    }

    /**
     * Test of getCityId method, of class Address.
     */
    @Test
    public void testGetCityId()
    {
        System.out.println("getCityId");
        Address instance = new Address();
        City expResult = null;
        City result = instance.getCityId();
        assertEquals(expResult, result);
    }

    /**
     * Test of setCityId method, of class Address.
     */
    @Test
    public void testSetCityId()
    {
        System.out.println("setCityId");
        City cityId = new City();
        Address instance = new Address();
        instance.setCityId(cityId);
        City result = instance.getCityId();
        assertEquals(cityId, result);
    }

    /**
     * Test of update method, of class Address.
     */
    @Test
    public void testUpdate() throws Exception
    {
        System.out.println("update");
        Address instance = new Address();
        String address = "1742 Evergreen Terrace";
        instance.setAddress("93 Rue de Rivoli");
        instance.refresh();
        instance.setAddress(address);
        instance.update();
        String result = instance.getAddress();
        assertEquals(address, result);
    }

    /**
     * Test of add method, of class Address.
     */
    @Test
    public void testAdd() throws Exception
    {
        System.out.println("add");
        Address instance = new Address();
        instance.setCityId(city);
        instance.setAddress("1742 Evergreen Terrace");
        instance.setPostalCode("90210");
        instance.setPhone("916-555-4321");
        instance.add();

        int result = instance.getAddressId();
        assertNotEquals(0, result);
    }

    /**
     * Test of delete method, of class Address.
     */
    @Test
    public void testDelete() throws Exception
    {
        System.out.println("delete");
        Address instance = new Address();
        instance.setAddress("93 Rue de Rivoli");
        instance.refresh();
        instance.delete();
    }

    /**
     * Test of refresh method, of class Address.
     */
    @Test
    public void testRefresh() throws Exception
    {
        System.out.println("refresh");
        Address instance = new Address();
        instance.setAddress("93 Rue de Rivoli");
        instance.refresh();

        int result = instance.getAddressId();
        assertNotEquals(0, result);
    }
}
