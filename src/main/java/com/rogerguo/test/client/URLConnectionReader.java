package com.rogerguo.test.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rogerguo.test.benchmark.PortoTaxiRealData;
import com.rogerguo.test.common.TrajectoryPoint;
import com.rogerguo.test.index.predicate.IdTemporalQueryPredicate;
import org.apache.commons.io.IOUtils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * @author yangguo
 * @create 2022-06-23 10:28 AM
 **/
public class URLConnectionReader {

    private static ObjectMapper objectMapper = new ObjectMapper();

    public static void main(String[] args) throws Exception {
        testAsyncInsertion();
    }

    public static void testAsyncInsertion() throws Exception {
        String urlString = "http://localhost:8001/asyncinsertion";



        String dataFile = "/home/yangguo/Data/DataSet/Trajectory/TaxiPorto/archive/porto_data_v1_1000w.csv";
        PortoTaxiRealData portoTaxiRealData = new PortoTaxiRealData(dataFile);

        TrajectoryPoint point;
        int count = 0;
        List<TrajectoryPoint> buffer = new ArrayList<>();
        while ((point = portoTaxiRealData.nextPointFromPortoTaxis()) != null) {
            buffer.add(point);
            count++;
            if (count % 1000000 == 0) {
                System.out.println("count: " + count);
            }
            if (buffer.size() == 2000) {

                String stringToSend = objectMapper.writeValueAsString(buffer);

                URL test = new URL(urlString);
                URLConnection yc = test.openConnection();
                yc.setDoOutput(true);
                OutputStreamWriter out = new OutputStreamWriter(
                        yc.getOutputStream());
                out.write(stringToSend);
                out.close();


                BufferedReader in = new BufferedReader(
                        new InputStreamReader(
                                yc.getInputStream()));
                String inputLine;

                while ((inputLine = in.readLine()) != null) {
                    //System.out.println(inputLine);
                }
                in.close();

                buffer.clear();
            }

        }
    }

    public static void testInsertion() throws Exception {
        String urlString = "http://localhost:8001/insertion";



        String dataFile = "/home/yangguo/Data/DataSet/Trajectory/TaxiPorto/archive/porto_data_v1_1000w.csv";
        PortoTaxiRealData portoTaxiRealData = new PortoTaxiRealData(dataFile);

        TrajectoryPoint point;
        int count = 0;
        List<TrajectoryPoint> buffer = new ArrayList<>();
        while ((point = portoTaxiRealData.nextPointFromPortoTaxis()) != null) {
            buffer.add(point);
            count++;
            if (count % 1000000 == 0) {
                System.out.println("count: " + count);
            }
            if (buffer.size() == 2000) {

                String stringToSend = objectMapper.writeValueAsString(buffer);

                URL test = new URL(urlString);
                URLConnection yc = test.openConnection();
                yc.setDoOutput(true);
                OutputStreamWriter out = new OutputStreamWriter(
                        yc.getOutputStream());
                out.write(stringToSend);
                out.close();


                BufferedReader in = new BufferedReader(
                        new InputStreamReader(
                                yc.getInputStream()));
                String inputLine;

                while ((inputLine = in.readLine()) != null) {
                    //System.out.println(inputLine);
                }
                in.close();

                buffer.clear();
            }

        }
    }

    public static void testIdTemporal() throws Exception {
        IdTemporalQueryPredicate idTemporalQueryPredicate = new IdTemporalQueryPredicate(1372673034000L,1372759434000L, "20000492");

        String predicateJson = objectMapper.writeValueAsString(idTemporalQueryPredicate);

        System.out.println(predicateJson);
        String urlString = "http://localhost:8001/query";

        URL test = new URL(urlString);
        String stringToSend = IOUtils.toString(predicateJson.getBytes(StandardCharsets.UTF_8), "UTF-8");

        URLConnection yc = test.openConnection();
        yc.setDoOutput(true);
        OutputStreamWriter out = new OutputStreamWriter(
                yc.getOutputStream());
        out.write(stringToSend);
        out.close();


        BufferedReader in = new BufferedReader(
                new InputStreamReader(
                        yc.getInputStream()));
        String inputLine;

        while ((inputLine = in.readLine()) != null)
            System.out.println(inputLine);
        in.close();
    }

}
