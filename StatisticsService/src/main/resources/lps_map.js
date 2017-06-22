function map(key, val) {		
	var lat1 = (this.latitude * Math.PI) / 180.0;
	var lon1 = (this.longitude * Math.PI) / 180.0;	
	var lat2 = <{LAT}>;
	var lon2 = <{LON}>;		
	var dist = Math.acos(Math.sin(lat1) * Math.sin(lat2) + Math.cos(lat1) * Math.cos(lat2) * Math.cos(lon1 - lon2)) * 6371000;
	
	if((dist < <{DIST}> || <{NODIST}> ) && (this.arrival.time >= <{TIMEMIN}> ||  <{NOMINTIME}>) && (this.left.time <= <{TIMEMAX}> || <{NOMAXTIME}>))  {
		if(this.licensePlateId != null && this.licensePlateId.length >= 2){
			var index = this.licensePlateId.indexOf('-');
			if(index > -1){
				var id = this.licensePlateId.substring(0,index);
				emit(id, 1);		
			}
		}
	}
}
