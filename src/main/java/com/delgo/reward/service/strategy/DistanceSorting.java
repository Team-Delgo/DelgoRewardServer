package com.delgo.reward.service.strategy;

import com.delgo.reward.mongoDomain.mungple.MongoMungple;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;

import java.util.Comparator;
import java.util.List;

public class DistanceSorting implements MungpleSortingStrategy {
    private final double latitude;
    private final double longitude;

    public DistanceSorting(String latitude, String longitude) {
        this.latitude = Double.parseDouble(latitude);
        this.longitude = Double.parseDouble(longitude);
    }

    @Override
    public List<MongoMungple> sort(List<MongoMungple> mungpleList) {
        // 거리 순 정렬 구현
        GeoJsonPoint targetPoint = new GeoJsonPoint(longitude, latitude);
        return mungpleList.stream()
                .sorted(Comparator.comparingDouble(mungple -> hversineCalculate(mungple.getLocation(), targetPoint)))
                .toList();
    }

    // 2차 평면 거리 계산 함수
    private double calculateDistance(GeoJsonPoint point1, GeoJsonPoint point2) {
        double xDiff = point1.getX() - point2.getX();
        double yDiff = point1.getY() - point2.getY();

        return Math.sqrt(xDiff * xDiff + yDiff * yDiff);
    }

    // 2차 구 거리 계산 함수 (실제 거리에 가까움)
    private double hversineCalculate(GeoJsonPoint point1, GeoJsonPoint point2) {
        int EARTH_RADIUS = 6371; // 지구의 반경, km
        double dLat = Math.toRadians(point2.getY() - point1.getY());
        double dLng = Math.toRadians(point2.getX() - point1.getX());

        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(Math.toRadians(point1.getY())) * Math.cos(Math.toRadians(point2.getY())) *
                        Math.sin(dLng / 2) * Math.sin(dLng / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return EARTH_RADIUS * c;
    }
}
