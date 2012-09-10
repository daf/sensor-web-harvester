package com.axiomalaska.sos.source.data

  // 65 types
object SensorPhenomenonIds {

  val BATTERY = 54
  val BATTERY_MAXIMUM = 55
  val BATTERY_MINIMUM = 56
    
  val SOLAR_RADIATION = 58
  val SOLAR_RADIATION_AVERAGE = 57
  val SOLAR_RADIATION_MAXIMUM = 59
  val SOLAR_RADIATION_MINIMUM = 60
    
  val RELATIVE_HUMIDITY = 15
  val RELATIVE_HUMIDITY_MAXIMUM = 40
  val RELATIVE_HUMIDITY_MINIMUM = 41
  val RELATIVE_HUMIDITY_AVERAGE = 42
    
  val DISSOLVED_OXYGEN_SATURATION = 61
  val DISSOLVED_OXYGEN = 34
    
  val AIR_TEMPERATURE = 2
  val AIR_TEMPERATURE_AVERAGE = 35
  val AIR_TEMPERATURE_MAXIMUM = 36
  val AIR_TEMPERATURE_MINIMUM = 37
    
  val WIND_GUST_DIRECTION = 46
  val WIND_GUST = 22
  val WIND_SPEED = 23
  val WIND_VERTICAL_VELOCITY = 45
  val WIND_DIRECTION = 21
    
  val WIND_WAVE_PERIOD = 24
  val WIND_WAVE_HEIGHT = 49
  val WIND_WAVE_DIRECTION = 50
  val SWELL_PERIOD = 25
  val SWELL_HEIGHT = 48
  val SWELL_WAVE_DIRECTION = 51
  val DOMINANT_WAVE_DIRECTION = 52
  val DOMINANT_WAVE_PERIOD = 53
  val SIGNIFICANT_WAVE_HEIGHT = 26
  val AVERAGE_WAVE_PERIOD = 47
    
  val CURRENT_DIRECTION = 7
  val CURRENT_SPEED = 8
    
  val AIR_CO2 = 63
  val SEA_WATER_CO2 = 62
    
  val PH_WATER = 33
  val SEA_WATER_TEMPERATURE = 20
  val DEW_POINT = 9
  val WATER_TEMPERATURE_INTRAGRAVEL = 44
    
  val SNOW_PILLOW = 64
  val SNOW_DEPTH = 65
  val SNOW_WATER_EQUIVALENT = 66
  val PRECIPITATION_INCREMENT = 38
  val PRECIPITATION_ACCUMULATION =39
    
  val REFLECTED_SHORTWAVE_RADIATION = 67
  val INCOMING_SHORTWAVE_RADIATION = 68
  val PHOTOSYNTHETICALLY_ACTIVE_RADIATION = 69
    
  val WIND_GENERATOR_CURRENT = 70
  val SALINITY = 16
  val PANEL_TEMPERATURE = 71
  val WEBCAM = 81
  val CONDUCTIVITY = 5
  val REAL_DIELECTRIC_CONSTANT = 72
  val BAROMETRIC_PRESSURE = 1
  val FUEL_MOISTURE = 73
  val WATER_TURBIDITY =17
  val FUEL_TEMPERATURE = 74
  val STREAM_FLOW = 75
  val SOIL_MOISTURE_PERCENT = 76
  val GROUND_TEMPERATURE_OBSERVED = 77
  
  val DEPTH_TO_WATER_LEVEL = 78
   
  val SEA_FLOOR_DEPTH_BELOW_SEA_SURFACE = 19
    
  val RESERVOIR_WATER_SURFACE_ABOVE_DATUM = 79
  val STREAM_GAGE_HEIGHT = 80
  val WATER_LEVEL = 32
  val WATER_LEVEL_PREDICTIONS = 43
  
  val CHLOROPHYLL_FLUORESCENCE = 82
  val ORTHOPHOSPHATE = 83
  val AMMONIUM = 84
  val NITRITE = 85
  val NITRATE = 86
  val NITRITE_PLUS_NITRATE = 87
  val CHLOROPHYLL = 88
  
  // new types used by STORET
  val ALUMINUM = 89
  val IRON = 90
  val ALKALINITY_HCB = 91
  val FRESH_WATER_TEMPERATURE = 92
  val COPPER = 93
  val ZINC = 94
  val FRESH_WATER_CONDUCTIVITY = 95
}