/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dtoscheduler.Models;

import dtoscheduler.model.Country;
import dtoscheduler.singleton.GlobalInformation;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Daniel
 */
public class CountryTest extends TestBase
{

    public CountryTest()
    {
        super();
    }

    @Override
    public void setUp() throws Exception
    {
        setUpCountry();
    }

    /**
     * Test of getCountryId method, of class Country.
     */
    @Test
    public void testGetCountryId()
    {
        System.out.println("getCountryId");
        Country instance = new Country();
        int expResult = 0;
        int result = instance.getCountryId();
        assertEquals(expResult, result);
    }

    /**
     * Test of getCountry method, of class Country.
     */
    @Test
    public void testGetCountry()
    {
        System.out.println("getCountry");
        Country instance = new Country();
        String expResult = "";
        String result = instance.getCountry();
        assertEquals(expResult, result);
    }

    /**
     * Test of setCountry method, of class Country.
     */
    @Test
    public void testSetCountry()
    {
        System.out.println("setCountry");
        String country = "France";
        Country instance = new Country();
        instance.setCountry(country);
    }

    /**
     * Test of getCreateDate method, of class Country.
     */
    @Test
    public void testGetCreateDate()
    {
        System.out.println("getCreateDate");
        Country instance = new Country();
        ZonedDateTime expResult = ZonedDateTime.now().truncatedTo(ChronoUnit.SECONDS);
        ZonedDateTime result = instance.getCreateDate().truncatedTo(ChronoUnit.SECONDS);
        assertEquals(expResult, result);
    }

    /**
     * Test of setCreateDate method, of class Country.
     */
    @Test
    public void testSetCreateDate()
    {
        System.out.println("setCreateDate");
        ZonedDateTime createDate = ZonedDateTime.now().truncatedTo(ChronoUnit.SECONDS);
        Country instance = new Country();
        instance.setCreateDate(createDate);
        ZonedDateTime result = instance.getCreateDate().truncatedTo(ChronoUnit.SECONDS);
        assertEquals(createDate, result);
    }

    /**
     * Test of getCreatedBy method, of class Country.
     */
    @Test
    public void testGetCreatedBy()
    {
        System.out.println("getCreatedBy");
        Country instance = new Country();
        String expResult = GlobalInformation.getInstance().getUser();
        String result = instance.getCreatedBy();
        assertEquals(expResult, result);
    }

    /**
     * Test of setCreatedBy method, of class Country.
     */
    @Test
    public void testSetCreatedBy()
    {
        System.out.println("setCreatedBy");
        String createdBy = "foo";
        Country instance = new Country();
        instance.setCreatedBy(createdBy);
        String result = instance.getCreatedBy();
        assertEquals(createdBy, result);
    }

    /**
     * Test of getLastUpdate method, of class Country.
     */
    @Test
    public void testGetLastUpdate()
    {
        System.out.println("getLastUpdate");
        Country instance = new Country();
        ZonedDateTime expResult = ZonedDateTime.now().truncatedTo(ChronoUnit.SECONDS);
        ZonedDateTime result = instance.getLastUpdate().truncatedTo(ChronoUnit.SECONDS);
        assertEquals(expResult, result);
    }

    /**
     * Test of setLastUpdate method, of class Country.
     */
    @Test
    public void testSetLastUpdate()
    {
        System.out.println("setLastUpdate");
        ZonedDateTime lastUpdate = ZonedDateTime.now().truncatedTo(ChronoUnit.SECONDS);
        Country instance = new Country();
        instance.setLastUpdate(lastUpdate);
        ZonedDateTime result = instance.getCreateDate().truncatedTo(ChronoUnit.SECONDS);
        assertEquals(lastUpdate, result);
    }

    /**
     * Test of getLastUpdateBy method, of class Country.
     */
    @Test
    public void testGetLastUpdateBy()
    {
        System.out.println("getLastUpdateBy");
        Country instance = new Country();
        String expResult = GlobalInformation.getInstance().getUser();
        String result = instance.getLastUpdateBy();
        assertEquals(expResult, result);
    }

    /**
     * Test of setLastUpdateBy method, of class Country.
     */
    @Test
    public void testSetLastUpdateBy()
    {
        System.out.println("setLastUpdateBy");
        String lastUpdateBy = "foo";
        Country instance = new Country();
        instance.setLastUpdateBy(lastUpdateBy);
        String result = instance.getLastUpdateBy();
        assertEquals(lastUpdateBy, result);
    }

    /**
     * Test of update method, of class Country.
     */
    @Test
    public void testUpdate() throws Exception
    {
        System.out.println("update");
        Country instance = new Country();
        instance.setCountry("France");
        instance.refresh();
        instance.update();
    }

    /**
     * Test of add method, of class Country.
     */
    @Test
    public void testAdd() throws Exception
    {
        System.out.println("add");
        Country instance = new Country();
        instance.setCountry("France");
        instance.add();

        // Once added, the item is refreshed, and the id will be set
        assertNotEquals(0, instance.getCountryId());
    }

    /**
     * Test of delete method, of class Country.
     */
    @Test
    public void testDelete() throws Exception
    {
        System.out.println("delete");
        Country instance = new Country();
        instance.setCountry("France");
        instance.lookup();
        instance.delete();
    }

    /**
     * Test of refresh method, of class Country.
     */
    @Test
    public void testRefresh() throws Exception
    {
        System.out.println("refresh");
        Country instance = new Country();
        instance.setCountry("France");
        instance.refresh();
        // the id will be set after refresh
        assertNotEquals(0, instance.getCountryId());
    }
}
