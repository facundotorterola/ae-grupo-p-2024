
import pandas as pd 
import numpy as np

contenedores = pd.read_csv('pruebaContenedoresFiltrado.csv')

contenedores = contenedores[contenedores['demandaPonderada'] > 0]
contenedores = contenedores.drop(columns= ['id'])   

min = contenedores['demandaPonderada'].min()
max = contenedores['demandaPonderada'].max()

print(f"min {min}, max {max}")

contenedores['demandaPonderada_normalizada'] = (contenedores['demandaPonderada'] - min ) / ( max - min)



id = [0] * len(contenedores) 


for i in range((len(contenedores))):
    id[i] = i+1

contenedores.loc[:, 'id'] = [id[i] for i in range(len(id))]

contenedores.to_csv('ContenedoresFiltradosDemandaPonderada.csv', index=False)