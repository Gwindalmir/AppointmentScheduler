/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dtoscheduler.Models;

import dtoscheduler.model.Customer;
import dtoscheduler.model.Address;
import dtoscheduler.model.Country;
import dtoscheduler.model.City;
import com.mysql.cj.jdbc.MysqlDataSource;
import dtoscheduler.singleton.GlobalInformation;
import dtoscheduler.singleton.SQLHelper;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;

/**
 *
 * @author Daniel
 */
public class TestBase
{
    public TestBase()
    {
    }

    @BeforeClass
    public static void setUpClass() throws Exception
    {
        // Load the data source
        InputStream fis = dtoscheduler.DTOScheduler.class.getResourceAsStream("Resources/db.properties");
        Properties props = new Properties();
        props.load(fis);

        MysqlDataSource ds = new MysqlDataSource();
        ds.setURL("jdbc:mysql://" + props.getProperty("MYSQL_HOSTNAME") + ":" + props.getProperty("MYSQL_PORT") + "/" + props.getProperty("MYSQL_CATALOG") + "?" + props.getProperty("MYSQL_ADDITIONAL"));
        ds.setUser(props.getProperty("MYSQL_USER"));
        ds.setPassword(props.getProperty("MYSQL_PASSWORD"));
        Connection conn = ds.getConnection();
        conn.setAutoCommit(false);
        SQLHelper.getInstance().setConnection(conn);

        GlobalInformation.getInstance().setUser("test");
    }

    @AfterClass
    public static void tearDownClass() throws Exception
    {
        SQLHelper.getInstance().getConnection().rollback();
        SQLHelper.getInstance().getConnection().close();
    }

    @Before
    public void setUp() throws Exception
    {
    }

    @After
    public void tearDown() throws Exception
    {
    }

    protected Country setUpCountry() throws SQLException
    {
        Country country = new Country();
        country.setCountry("France");

        if (!country.lookup())
            country.add();

        return country;
    }

    protected City setUpCity(Country country) throws SQLException
    {
        City city = new City();
        city.setCity("Paris");
        city.setCountryId(country);

        if (!city.lookup())
            city.add();

        return city;
    }

    protected Address setUpAddress(City city) throws SQLException
    {
        Address address = new Address();
        address.setCityId(city);
        address.setAddress("93 Rue de Rivoli");
        address.setPostalCode("75001");
        address.setPhone("+33 (0)1 40 20 50 50");

        if (!address.lookup())
            address.add();

        return address;
    }

    protected Customer setUpCustomer(Address address) throws SQLException
    {
        Customer customer = new Customer();
        customer.setAddressId(address);
        customer.setCustomerName("Jean Luc Picard");
        customer.setActive(true);

        if (!customer.lookup())
            customer.add();

        return customer;
    }
}
