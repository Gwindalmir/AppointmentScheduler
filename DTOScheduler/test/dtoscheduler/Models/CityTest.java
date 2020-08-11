/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dtoscheduler.Models;

import dtoscheduler.model.Country;
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
public class CityTest extends TestBase
{
    private Country country;

    public CityTest()
    {
        super();
    }

    @Override
    public void setUp() throws Exception
    {
        country = setUpCountry();
        setUpCity(country);
    }

    /**
     * Test of getCityId method, of class City.
     */
    @Test
    public void testGetCityId()
    {
        System.out.println("getCityId");
        City instance = new City();
        int expResult = 0;
        int result = instance.getCityId();
        assertEquals(expResult, result);
    }

    /**
     * Test of getCity method, of class City.
     */
    @Test
    public void testGetCity()
    {
        System.out.println("getCity");
        City instance = new City();
        String expResult = "";
        String result = instance.getCity();
        assertEquals(expResult, result);
    }

    /**
     * Test of setCity method, of class City.
     */
    @Test
    public void testSetCity()
    {
        System.out.println("setCity");
        String city = "France";
        City instance = new City();
        instance.setCity(city);
        String result = instance.getCity();
        assertEquals(city, result);
    }

    /**
     * Test of getCreateDate method, of class City.
     */
    @Test
    public void testGetCreateDate()
    {
        System.out.println("getCreateDate");
        City instance = new City();
        ZonedDateTime expResult = ZonedDateTime.now().truncatedTo(ChronoUnit.SECONDS);
        ZonedDateTime result = instance.getCreateDate().truncatedTo(ChronoUnit.SECONDS);
        assertEquals(expResult, result);
    }

    /**
     * Test of setCreateDate method, of class City.
     */
    @Test
    public void testSetCreateDate()
    {
        System.out.println("setCreateDate");
        ZonedDateTime createDate = ZonedDateTime.now().truncatedTo(ChronoUnit.SECONDS);
        City instance = new City();
        instance.setCreateDate(createDate);
        ZonedDateTime result = instance.getCreateDate().truncatedTo(ChronoUnit.SECONDS);
        assertEquals(createDate, result);
    }

    /**
     * Test of getCreatedBy method, of class City.
     */
    @Test
    public void testGetCreatedBy()
    {
        System.out.println("getCreatedBy");
        City instance = new City();
        String expResult = GlobalInformation.getInstance().getUser();
        String result = instance.getCreatedBy();
        assertEquals(expResult, result);
    }

    /**
     * Test of setCreatedBy method, of class City.
     */
    @Test
    public void testSetCreatedBy()
    {
        System.out.println("setCreatedBy");
        String createdBy = "foo";
        City instance = new City();
        instance.setCreatedBy(createdBy);
        String result = instance.getCreatedBy();
        assertEquals(createdBy, result);
    }

    /**
     * Test of getLastUpdate method, of class City.
     */
    @Test
    public void testGetLastUpdate()
    {
        System.out.println("getLastUpdate");
        City instance = new City();
        ZonedDateTime expResult = ZonedDateTime.now().truncatedTo(ChronoUnit.SECONDS);
        ZonedDateTime result = instance.getLastUpdate().truncatedTo(ChronoUnit.SECONDS);
        assertEquals(expResult, result);
    }

    /**
     * Test of setLastUpdate method, of class City.
     */
    @Test
    public void testSetLastUpdate()
    {
        System.out.println("setLastUpdate");
        ZonedDateTime lastUpdate = ZonedDateTime.now().truncatedTo(ChronoUnit.SECONDS);
        City instance = new City();
        instance.setLastUpdate(lastUpdate);
        ZonedDateTime result = instance.getCreateDate().truncatedTo(ChronoUnit.SECONDS);
        assertEquals(lastUpdate, result);
    }

    /**
     * Test of getLastUpdateBy method, of class City.
     */
    @Test
    public void testGetLastUpdateBy()
    {
        System.out.println("getLastUpdateBy");
        City instance = new City();
        String expResult = GlobalInformation.getInstance().getUser();
        String result = instance.getLastUpdateBy();
        assertEquals(expResult, result);
    }

    /**
     * Test of setLastUpdateBy method, of class City.
     */
    @Test
    public void testSetLastUpdateBy()
    {
        System.out.println("setLastUpdateBy");
        String lastUpdateBy = "foo";
        City instance = new City();
        instance.setLastUpdateBy(lastUpdateBy);
        String result = instance.getLastUpdateBy();
        assertEquals(lastUpdateBy, result);
    }

    /**
     * Test of getCountryId method, of class City.
     */
    @Test
    public void testGetCountryId()
    {
        System.out.println("getCountryId");
        City instance = new City();
        Country expResult = null;
        Country result = instance.getCountryId();
        assertEquals(expResult, result);
    }

    /**
     * Test of setCountryId method, of class City.
     */
    @Test
    public void testSetCountryId()
    {
        System.out.println("setCountryId");
        Country countryId = new Country();
        City instance = new City();
        instance.setCountryId(countryId);
        Country result = instance.getCountryId();
        assertEquals(countryId, result);
    }

    /**
     * Test of update method, of class City.
     */
    @Test
    public void testUpdate() throws Exception
    {
        System.out.println("update");
        City instance = new City();
        String cityName = "Versailles";
        instance.setCity("Paris");
        instance.refresh();
        instance.setCity(cityName);
        instance.update();

        String result = instance.getCity();
        assertEquals(cityName, result);
    }

    /**
     * Test of add method, of class City.
     */
    @Test
    public void testAdd() throws Exception
    {
        System.out.println("add");
        City instance = new City();
        instance.setCity("Nice");
        instance.setCountryId(country);
        instance.add();

        int result = instance.getCityId();
        assertNotEquals(0, result);
    }

    /**
     * Test of delete method, of class City.
     */
    @Test
    public void testDelete() throws Exception
    {
        System.out.println("delete");
        City instance = new City();
        instance.setCity("Paris");
        instance.refresh();
        instance.delete();
    }

    /**
     * Test of refresh method, of class City.
     */
    @Test
    public void testRefresh() throws Exception
    {
        System.out.println("refresh");
        City instance = new City();
        instance.setCity("Paris");
        instance.refresh();

        int result = instance.getCityId();
        assertNotEquals(0, result);
    }

}
