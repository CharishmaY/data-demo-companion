package org.msse.demo.common.util;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class KdTreeTest {

  static class DataPoint extends KdTree.XYZPoint {

    private String name;

    public DataPoint(double x, double y, double z, String name) {
      super(x, y, z);
      this.name = name;
    }

    public String name() {
      return name;
    }

  }

  @Test
  void nearest() {

    KdTree<DataPoint> tree = new KdTree<>();

    data().forEach(tree::add);

    Collection<DataPoint> points = tree.nearestNeighbourSearch(1, new DataPoint(44.8849, -93.2131, 0, null));

    Assertions.assertEquals("U.S. Bank Stadium", points.iterator().next().name);
  }


  private static List<DataPoint> data() {

    final List<DataPoint> list = new ArrayList<>();

    try {
      CSVFormat format = CSVFormat.Builder.create().setDelimiter(",").setHeader().build();

      InputStream input = Thread.currentThread().getContextClassLoader().getResourceAsStream("venues.csv");
      InputStreamReader reader = new InputStreamReader(input);

      CSVParser parser = new CSVParser(reader, format);

      //name,street,city,state,zip,lat,lat,capacity

      for (CSVRecord rec : parser) {

        DataPoint point = new DataPoint(Double.parseDouble(rec.get("lat")), Double.parseDouble(rec.get("lon")), 0.0, rec.get("name"));

        list.add(point);

      }

      parser.close();

    } catch (final IOException e) {
      throw new RuntimeException(e);
    }

    return list;
  }

}