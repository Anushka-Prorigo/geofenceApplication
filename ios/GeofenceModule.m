// GeofenceModule.m

#import <React/RCTBridgeModule.h>
#import <React/RCTEventEmitter.h>

@interface RCT_EXTERN_MODULE(GeofenceModule, RCTEventEmitter)

RCT_EXTERN_METHOD(startMonitoringGeofence:(double)withlatitude withLongitude:(double)lon withRadius:(double)radius)

@end
