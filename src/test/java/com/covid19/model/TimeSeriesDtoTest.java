//package com.statistics.corona.model;
//
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.springframework.boot.test.context.SpringBootTest;
//
//import java.util.LinkedHashMap;
//
//import static org.assertj.core.api.Assertions.assertThat;
//
//@SpringBootTest
//@DisplayName("TimeSeriesDto tests")
//public class TimeSeriesDtoTest {
//
//    private final TimeSeriesDto timeSeriesDto = new TimeSeriesDto();
//
//    @BeforeEach
//    public void setUp() {
//        timeSeriesDto.setProvince("Hessen");
//        timeSeriesDto.setCountry("Germany");
//        LinkedHashMap<String, Integer> dataMap = new LinkedHashMap<>();
//        dataMap.put("0000.00.00", 1);
//        timeSeriesDto.setDataMap(dataMap);
//    }
//
//    @Test
//    @DisplayName("Check timeSeriesDto mapping")
//    public void checkTimeSeriesDtoMapping() {
//        TimeSeriesDto timeSeriesDtoMapping = new TimeSeriesDto(timeSeriesDto);
//        assertThat(timeSeriesDtoMapping.getCountry()).isEqualTo(timeSeriesDto.getCountry());
//        assertThat(timeSeriesDtoMapping.getProvince()).isEqualTo(timeSeriesDto.getProvince());
//        assertThat(timeSeriesDtoMapping.getDataMap()).isEqualTo(timeSeriesDto.getDataMap());
//    }
//
//    /*@Test
//    @DisplayName("Test toString method of timeSeriesDto")
//    public void toStringTest() {
//        assertThat(timeSeriesDto.toString())
//                .isEqualTo("TimeSeriesDto{province='Hessen', " +
//                        "country='Germany', " +
//                        "dataMap={0000.00.00=1}}");
//    }*/
//}