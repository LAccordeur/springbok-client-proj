package com.rogerguo.test.client;

import com.rogerguo.test.benchmark.PortoTaxiRealData;
import com.rogerguo.test.common.SpatialBoundingBox;
import com.rogerguo.test.common.TrajectoryPoint;
import com.rogerguo.test.index.predicate.IdTemporalQueryPredicate;
import com.rogerguo.test.index.predicate.SpatialTemporalRangeQueryPredicate;

import java.util.List;

/**
 * @author yangguo
 * @create 2022-06-28 3:09 PM
 **/
public class VerificationUtil {
    public static void verifyIdTemporalResult(List<TrajectoryPoint> resultFromSpringbok, IdTemporalQueryPredicate predicate) {
        int matchedCount = 0;
        int actualCount = 0;

        System.out.println(predicate);

        TrajectoryPoint point = null;
        int count = 0;
        PortoTaxiRealData portoTaxiRealData = new PortoTaxiRealData("/home/yangguo/Data/DataSet/Trajectory/TaxiPorto/archive/porto_data_v1_1000w.csv");

        while ((point = portoTaxiRealData.nextPointFromPortoTaxis()) != null) {
            count++;
            if (predicate.getDeviceId().equals(point.getOid()) && predicate.getStartTimestamp() <= point.getTimestamp()
                    && predicate.getStopTimestamp() >= point.getTimestamp()) {
                actualCount = actualCount + 1;

                if (containPoint(resultFromSpringbok, point)) {
                    matchedCount++;
                } else {
                    System.out.println(point);
                }
            }

            if (count > 15000000) {
                break;
            }
        }

        System.out.println("actual count: " + actualCount);
        System.out.println("matched count: " + matchedCount);
        System.out.println();
    }

    public static void verifyRangeResult(List<TrajectoryPoint> resultFromSpringbok, SpatialTemporalRangeQueryPredicate predicate) {
        int matchedCount = 0;
        int actualCount = 0;

        System.out.println(predicate);

        TrajectoryPoint point = null;
        int count = 0;
        PortoTaxiRealData portoTaxiRealData = new PortoTaxiRealData("/home/yangguo/Data/DataSet/Trajectory/TaxiPorto/archive/porto_data_v1_1000w.csv");
        SpatialBoundingBox boundingBox = new SpatialBoundingBox(predicate.getLowerLeft(), predicate.getUpperRight());
        while ((point = portoTaxiRealData.nextPointFromPortoTaxis()) != null) {
            count++;
            if (SpatialBoundingBox.checkBoundingBoxContainPoint(boundingBox, point) && predicate.getStartTimestamp() <= point.getTimestamp()
                    && predicate.getStopTimestamp() >= point.getTimestamp()) {
                actualCount = actualCount + 1;

                if (containPoint(resultFromSpringbok, point)) {
                    matchedCount++;
                } else {
                    System.out.println(point);
                }
            }

            if (count > 15000000) {
                break;
            }
        }

        System.out.println("actual count: " + actualCount);
        System.out.println("matched count: " + matchedCount);
        System.out.println();
    }

    public static boolean containPoint(List<TrajectoryPoint> points, TrajectoryPoint checkPoint) {
        for (TrajectoryPoint point : points) {
            if (point.getOid().equals(checkPoint.getOid())
                    && point.getTimestamp() == checkPoint.getTimestamp()
                    && Math.abs(point.getLongitude() - checkPoint.getLongitude()) < 0.000000001
                    && Math.abs(point.getLatitude() - checkPoint.getLatitude()) < 0.000000001) {
                return true;
            }
        }
        return false;
    }
}
