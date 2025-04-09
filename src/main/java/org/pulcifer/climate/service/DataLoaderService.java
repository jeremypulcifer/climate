package org.pulcifer.climate.service;

import org.springframework.stereotype.Service;

@Service public class DataLoaderService {


//    @Autowired
//    CityWeatherRepository weatherRepo;
//    @Autowired
//    CityRepository cityRepo;
//    @Autowired
//    CityMetaRepository cityMetaRepo;
//
//    public String loadCityMetaFromLegacyCities() {
//        cityMetaRepo.deleteAll();
//        List<CityMeta> cityMetas = cityRepo.findAll().stream().map(CityMeta::new).toList();
//        cityMetaRepo.saveAll(cityMetas);
//        return "loaded " + cityMetas.size() + " city metadata records";
//    }
//
//    public void loadSeedFromDisk() {
//        loadSeed();
//    }
//
//    public String loadFromDisk() {
//
//        int i = 0;
//        loadSeed();
//
//        String directoryPath = "/Users/jdp/climate-data/new";
//
//        File directory = new File(directoryPath);
//        ObjectMapper objectMapper = new ObjectMapper();
//
//        if (directory.exists() && directory.isDirectory()) {
//            File[] files = directory.listFiles((dir, name) -> name.endsWith(".json"));
//
//            if (files != null) {
//
//                weatherRepo.deleteAll();
//
//                for (File file : files) {
//                    try {
//                        String content = new String(Files.readAllBytes(Paths.get(file.getPath())));
//                        CityWeather cityWeather = objectMapper.readValue(content, CityWeather.class);
//
//                        String fileName = file.getName();
//                        String nameWithoutExtensionStr = fileName.contains(".")
//                                ? fileName.substring(0, fileName.lastIndexOf('.'))
//                                : fileName;
//                        Integer nameWithoutExtension = Integer.parseInt(nameWithoutExtensionStr);
//                        CityMeta cityMeta = cityLocations.get(nameWithoutExtension);
//                        if (cityMeta == null) {
//                            throw new RuntimeException("Can't find seed data for seed id "+nameWithoutExtension);
//                        }
//
//                        cityWeather.setSeedId(cityMeta.getCityId());
//                        cityWeather.setCity(cityMeta.getCity());
//                        cityWeather.setCountry(cityMeta.getCountry());
//
//                        i++;
//
//                        weatherRepo.save(cityWeather);
//
//                    } catch (IOException e) {
//                        System.out.println("Error reading or parsing JSON from " + file.getName());
//                        e.printStackTrace();
//                    }
//                }
//            } else {
//                System.out.println("No JSON files found in the directory.");
//            }
//        } else {
//            System.out.println("Invalid directory path.");
//        }
//
//
//        return "loaded "+ i+" weather responses";
//    }
//
//    Map<Integer, CityMeta> cityLocations = new HashMap<>();
//    protected void loadSeed() {
//
//        cityMetaRepo.deleteAll();
//
//        ClassLoader classloader = Thread.currentThread().getContextClassLoader();
//        InputStream inputStream = classloader.getResourceAsStream("SeedCitiesV2.csv");
//        InputStreamReader streamReader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
//
//        try (BufferedReader reader = new BufferedReader(streamReader)) {
//            String line = reader.readLine(); // skip header line
//            while ((line = reader.readLine()) != null) {
//                CityMeta cityMeta = new CityMeta(line);
//                cityLocations.put(cityMeta.getCityId(), cityMeta);
//                cityMetaRepo.save(cityMeta);
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }

//    protected void loadSeedV2() {
//
//        cityMetaRepo.deleteAll();
//
//        ClassLoader classloader = Thread.currentThread().getContextClassLoader();
//        InputStream inputStream = classloader.getResourceAsStream("SeedCitiesV2.csv");
//        InputStreamReader streamReader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
//
//        try (BufferedReader reader = new BufferedReader(streamReader)) {
//            String line = reader.readLine(); // skip header line
//            while ((line = reader.readLine()) != null) {
//                CityMeta cityMeta = new CityMeta(line);
//                cityLocations.put(cityMeta.getCityId(), cityMeta);
//                cityMetaRepo.save(cityMeta);
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
}
