import pandas as pd 
import math
import numpy as np


def demandaContenedor(cont_x, cont_y, m_x, m_y, poblacion):

    demanda = 0
    for i in range(len(m_x)):
        distancia = calcularDistancia(cont_x, cont_y, m_x[i], m_y[i])
        if (distancia <= 200):
            demanda += poblacion[i]

    #print(f"El contenedor mas cercado a la manzana es el {masCercano}")
    return demanda


def calcularDistancia(x1, y1, x2, y2):
    from pyproj import Transformer
    transformer = Transformer.from_crs("epsg:4326", "epsg:32721", always_xy=True)

    transformer = Transformer.from_crs("epsg:4326", "epsg:32721", always_xy=True)

    # Transformar las coordenadas
    punto1_utm = transformer.transform(x1, y1)
    punto2_utm = transformer.transform(x2, y2)


    return math.sqrt((punto2_utm[0] - punto1_utm[0])**2 + (punto2_utm[1] - punto1_utm[1])**2)



contenedores = pd.read_csv('contenedores.csv')

manzanas = pd.read_csv('poblacionCoordenadas.csv')

manzanas_x = list(manzanas['x'])
manzanas_y = list(manzanas['y'])
poblacion = list(manzanas['P_TOT'])

cont_x = list(contenedores['x'])
cont_y = list(contenedores['y'])

demandas = np.zeros((len(cont_x),)) 


 
for i in range((len(cont_x))):
     
    demandaCont = demandaContenedor(cont_x[i], cont_y[i], manzanas_x, manzanas_y, poblacion)
    demandas[i] = demandaCont


contenedores.loc[:, 'demanda'] = [demandas[i] for i in range(len(demandas))]


contenedores = contenedores(contenedores['demanda'] > 0)

contenedores.to_csv('contenedoresFiltrados.csv', index=False)
