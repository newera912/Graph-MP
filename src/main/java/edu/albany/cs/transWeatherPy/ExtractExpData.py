
root="F:/workspace/git/Graph-MP/data/mesonet_data/mesonetExpData/"
metaPath=root + "/meta/"
coefPath=root + "/coefficients/"

sta_names={0:"BATA",1:"SBRI",2:"WATE",3:"JORD",4:"CSQR",5:"WEST",6:"COLD",7:"SPRA",8:"COBL",9:"STEP"}
coefVars=["tmin","tmax","tavg","sdtmin","sdtmax"]
metaData={}
for staName in sta_names.values():
    metaFile=metaPath+staName+".txt"
    with open(metaFile,"r") as mf:
        sLine=""
        for line in mf.readlines():
            sLine+=line.strip()+"_"
        terms=sLine.strip().split("_")
        """ 2: lat 3: lon 4:elevation 5:dist_to_water """
        metaData[terms[1]]=terms[2]+"_"+terms[3]+"_"+terms[4]+"_"+terms[5]
        
print metaData[sta_names[0]],sta_names[0]


print coefVars[0]
tmin=[[] for i in range(5)]
for i in [0,1,2,3,4]:
    with open(coefPath+coefVars[0]+str(i)+".txt","r") as cF0:
        tmin[i]=map(float,cF0.read().splitlines())
        #print len(tmin[i]),tmin[i]


print coefVars[1]        
tmax=[[] for i in range(5)]
for i in [0,1,2,3,4]:
    with open(coefPath+coefVars[1]+str(i)+".txt","r") as cF1:
        tmax[i]=map(float,cF1.read().splitlines())
        #print len(tmax[i]),tmax[i]


print coefVars[2]
tavg=[[] for i in range(5)]
for i in [0,1,2,3,4]:
    with open(coefPath+coefVars[2]+str(i)+".txt","r") as cF2:
        tavg[i]=map(float,cF2.read().splitlines())
        #print len(tavg[i]),tavg[i]


print coefVars[3]
stdmin=[[] for i in range(5)]
for i in [0,1,2,3,4]:
    with open(coefPath+coefVars[3]+str(i)+".txt","r") as cF3:
        stdmin[i]=map(float,cF3.read().splitlines())
        #print len(stdmin[i]),stdmin[i]
   
print coefVars[4]
stdmax=[[] for i in range(5)]
for i in [0,1,2,3,4]:
    with open(coefPath+coefVars[4]+str(i)+".txt","r") as cF4:
        stdmax[i]=map(float,cF4.read().splitlines())
        #print len(stdmax[i]),stdmax[i] 
        
""" intercept(200)+elevation(200)*elevation(sta)+lat(200)*lat(sta)+lon(200)*lon(sta)+waterdistance(200)*distance(sta). """
""" 0:intercept, 1:elevation, 2:lat, 3:lon, 4:distance to water """
for d in range(366):
    import datetime
    if d==59:
        continue    
    date=datetime.datetime.strptime('2016 '+str(d+1), '%Y %j')
    date=str(date.strftime('%Y%m%d'))
    print date
    if d>59:
        d=d-1
    with open(root+"/statExpData/"+str(date)+".txt","a+") as oF:
        for stat in range(len(sta_names.values())):
            """ 0: lat 1: lon 2:elevation 3:dist_to_water """
            
            sta_meta=map(float,metaData[sta_names[stat]].split("_"))
            #print date,stat,sta_names[stat],sta_meta        
            dayTavg=str(tavg[0][d]+tavg[1][d]*sta_meta[2]+tavg[2][d]*sta_meta[0]+tavg[3][d]*sta_meta[1]+tavg[4][d]*sta_meta[3])
            dayTmin=str(tmin[0][d]+tmin[1][d]*sta_meta[2]+tmin[2][d]*sta_meta[0]+tmin[3][d]*sta_meta[1]+tmin[4][d]*sta_meta[3])
            dayTmax=str(tmax[0][d]+tmax[1][d]*sta_meta[2]+tmax[2][d]*sta_meta[0]+tmax[3][d]*sta_meta[1]+tmax[4][d]*sta_meta[3])
            dayTstdmin=str(stdmin[0][d]+stdmin[1][d]*sta_meta[2]+stdmin[2][d]*sta_meta[0]+stdmin[3][d]*sta_meta[1]+stdmin[4][d]*sta_meta[3])
            dayTstdmax=str(stdmax[0][d]+stdmax[1][d]*sta_meta[2]+stdmax[2][d]*sta_meta[0]+stdmax[3][d]*sta_meta[1]+stdmax[4][d]*sta_meta[3])
            oF.write(dayTavg+" "+dayTmin+" "+dayTstdmin+" "+dayTmax+" "+dayTstdmax+"\n")
