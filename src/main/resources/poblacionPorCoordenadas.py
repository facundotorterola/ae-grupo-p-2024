import pandas as pd 
from shapely import wkt
import geopandas as gpd

df_coord = pd.read_csv('ine_zonas_11.csv')

df_pob = pd.read_csv('poblacionManzanas.csv')



df = pd.merge(df_pob, df_coord, on=['SECCION', 'SEGMENTO', 'ZONA'], how='inner')


valores_deseados = [1, 2, 3, 4, 5, 15, 16]
df = df[df['CCZ'].isin(valores_deseados)]


final = df[['P_TOT', 'geometry']]

geo = final['geometry']
print(wkt.loads(geo[0]).centroid)

centroids = map(lambda wkt_str: wkt.loads(wkt_str).centroid, geo)

centroids = list(centroids)


final_copy = final.copy()

final_copy.loc[:, 'x'] = [centroid.x for centroid in centroids]
final_copy.loc[:, 'y'] = [centroid.y for centroid in centroids]

final_copy= final_copy.drop(columns=('geometry'))

final_copy = final_copy[final_copy['P_TOT'] > 0]

print(final_copy)


final_copy.to_csv('poblacionCoordenadas.csv', index=True)