package com.lewisallen.rtdptiCache.tests;

import com.lewisallen.rtdptiCache.caches.NaPTANCache;
import com.lewisallen.rtdptiCache.caches.TrainStationCache;
import com.lewisallen.rtdptiCache.db.TransportDatabase;
import com.lewisallen.rtdptiCache.logging.ErrorHandler;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

public class TransportDatabaseTest
{
    ErrorHandler handler = new ErrorHandler();

    @Test
    void testDatabaseRetrieval()
    {
        TransportDatabase db = new TransportDatabase();

        try
        {
            Connection conn = db.getDbConnection();
            db.query(NaPTANCache.naptanQuery, conn);
            db.query(TrainStationCache.stationQuery, conn);
        }
        catch (SQLException | ClassNotFoundException e)
        {
            e.printStackTrace();
            Assertions.fail();
        }
    }

}
