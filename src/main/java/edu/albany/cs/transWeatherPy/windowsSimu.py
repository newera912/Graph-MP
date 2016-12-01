import numpy as np
import matplotlib.pyplot as plt
class prettyfloat(float):
    def __repr__(self):
        return "%0.2f" % self
data=[]
data.extend(np.random.normal(8,1,10))
data.extend(np.random.normal(16,1,26))
X=[i for i in range(len(data))]

print map(prettyfloat,data)
#data=[7.46, 9.60, 7.45, 9.03, 9.65, 8.42, 10.22, 8.79, 7.78, 7.99, 5.92, 7.94, 25.75, 24.91, 24.38, 25.55, 24.64, 26.69, 25.99, 24.59, 25.21, 24.85, 25.67, 25.84, 24.37, 26.20, 24.40, 24.37, 25.01, 25.61, 25.61, 24.47, 23.74, 22.53, 24.35, 24.20]
#data=[8.57, 9.24, 9.31, 7.87, 7.01, 7.76, 7.73, 7.40, 7.20, 8.51, 8.46, 7.75, 18.16, 16.34, 14.58, 14.95, 16.40, 16.05, 15.36, 15.16, 15.42, 17.28, 15.67, 17.15, 16.75, 15.10, 16.39, 15.83, 14.64, 16.69, 16.11, 17.56, 17.21, 16.68, 15.22, 15.92]
plt.plot(X,data)
plt.show()

T1=[10.23, 10.15, 9.93, 10.90, 9.37, 10.47, 24.86, 25.93, 25.56, 23.23, 25.45, 23.88, 26.57, 26.16, 25.13, 25.73, 25.18, 25.19, 24.57, 25.65, 27.21, 26.47, 23.49, 25.18, 27.04, 23.10, 23.29, 25.90, 25.01, 24.44, 25.26, 24.44, 22.56, 26.38, 26.26, 25.36]
print T1[:6],np.std(T1[:6])