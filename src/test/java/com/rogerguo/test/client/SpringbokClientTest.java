package com.rogerguo.test.client;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rogerguo.test.benchmark.PortoTaxiRealData;
import com.rogerguo.test.common.Point;
import com.rogerguo.test.common.TrajectoryPoint;
import com.rogerguo.test.index.predicate.IdTemporalQueryPredicate;
import com.rogerguo.test.index.predicate.SpatialTemporalRangeQueryPredicate;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SpringbokClientTest {

    private SpringbokClient client = new SpringbokClient("http://localhost:8001");

    private ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void insert() throws Exception {

        String dataFile = "/home/yangguo/Data/DataSet/Trajectory/TaxiPorto/archive/porto_data_v1_1000w.csv";
        PortoTaxiRealData portoTaxiRealData = new PortoTaxiRealData(dataFile);

        List<TrajectoryPoint> dataBuffer = new ArrayList<>();
        TrajectoryPoint point;
        while ((point = portoTaxiRealData.nextPointFromPortoTaxis()) != null) {
            dataBuffer.add(point);
            if (dataBuffer.size() == 20000) {
                client.insert(dataBuffer);
                dataBuffer.clear();
            }
        }
    }

    @Test
    void asyncInsert() throws Exception {
        String dataFile = "/home/yangguo/Data/DataSet/Trajectory/TaxiPorto/archive/porto_data_v1_1000w.csv";
        PortoTaxiRealData portoTaxiRealData = new PortoTaxiRealData(dataFile);

        List<TrajectoryPoint> dataBuffer = new ArrayList<>();
        TrajectoryPoint point;
        while ((point = portoTaxiRealData.nextPointFromPortoTaxis()) != null) {
            dataBuffer.add(point);
            if (dataBuffer.size() == 2000) {
                client.asyncInsert(dataBuffer);
                dataBuffer.clear();
            }
        }
    }

    @Test
    void idTemporalQuery() {

        IdTemporalQueryPredicate idTemporalQueryPredicate = new IdTemporalQueryPredicate(1372673034000L,1372759434000L, "20000492");
        try {
            String result = client.idTemporalQuery(idTemporalQueryPredicate);
            List<TrajectoryPoint> pointList = objectMapper.readValue(result, new TypeReference<List<TrajectoryPoint>>() {});
            System.out.println(result);
            System.out.println(pointList.size());
            VerificationUtil.verifyIdTemporalResult(pointList, idTemporalQueryPredicate);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Test
    void spatialTemporalQuery() {

        Point point1 = new Point(-8.621307, 41.204259);
        Point point2 = new Point(-8.611307, 41.214259);
        SpatialTemporalRangeQueryPredicate predicate = new SpatialTemporalRangeQueryPredicate(1372651679000L,1372673279000L, point1, point2);

        try {
            String result = client.spatialTemporalQuery(predicate);
            List<TrajectoryPoint> pointList = objectMapper.readValue(result, new TypeReference<List<TrajectoryPoint>>() {});
            System.out.println(result);
            System.out.println(pointList.size());
            VerificationUtil.verifyRangeResult(pointList, predicate);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Test
    void idTemporalQueryWithCompression() {
        IdTemporalQueryPredicate idTemporalQueryPredicate = new IdTemporalQueryPredicate(1372673034000L,1372759434000L, "20000492");
        try {
            String result = client.idTemporalQueryWithCompressionTransfer(idTemporalQueryPredicate);
            String[] records = result.split(";");
            System.out.println(result);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    void spatialTemporalQueryWithCompression() {

        Point point1 = new Point(-8.621307, 41.204259);
        Point point2 = new Point(-8.611307, 41.214259);
        SpatialTemporalRangeQueryPredicate predicate = new SpatialTemporalRangeQueryPredicate(1372651679000L,1372673279000L, point1, point2);

        try {
            String result = client.spatialTemporalQueryWithCompressionTransfer(predicate);

            System.out.println(result);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}