# # data={}
# # with open("F:/workspace/git/TranWeatherProject/data/output.txt","r") as oF:
# #     for line in oF.readlines():
# #         sta=line.strip().split("-")[0]
# #         mon=line.strip().split(".")[0][-6:]
# #         if data.has_key(sta):
# #             data[sta]+=" "+mon 
# #         else:
# #             data[sta]=mon
# # 
# # for d,v in data.items():
# #     print d,":-",v
# 
# import csv
# data={}
# with open("F:/workspace/git/TranWeatherProject/data/output.txt","r") as oF:
#     for line in oF.readlines():
#         fileName="F:/workspace/git/TranWeatherProject/data/data_request/"+line.strip()
#         
#         sta_y_m=line.strip().split(".")[0]
#         #print sta_y_m
#         with open(fileName,"r") as csvF:
#             datac=csv.reader(csvF)
#             next(datac, None)  # skip the headers
#             dayList=[]
#             for d in datac:          
#                 dayList.append(d[1].split("T")[0][-2:])
#             dayList=sorted(list(set(dayList)))
#             for day in dayList:
#                 if data.has_key(sta_y_m):
#                     data[sta_y_m]+=" "+day
#                 else:
#                     data[sta_y_m]=" "+day
# result=open("F:/workspace/git/TranWeatherProject/data/moutput.txt","w")
# 
# for k in sorted(data):
#     result.write(k+": "+data[k]+"\n")
#     print k,":-",data[k]
#                 
for count in range(12):
    for C in range(count+1):
        S = range(C, count+1)
        print S