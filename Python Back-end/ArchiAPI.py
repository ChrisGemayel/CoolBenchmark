# -*- coding: utf-8 -*-


import numpy as np
import pymongo
from flask import jsonify
from flask import Flask, request 
from flask_cors import CORS
app = Flask(__name__)
CORS(app)

jsonResponse=None


def geo_mean(iterable):
    a = np.array(iterable)
    return a.prod()**(1.0/len(a))


def ConnectionParameters():
    MongoDbServer     = '127.0.0.1'          
    MongoDbPort       = 27017
    MongoDbDatabase   = 'Benchmarks'         
    MongoDbCollection = 'ProductionTable'       

    connection= pymongo.MongoClient(MongoDbServer,MongoDbPort)
    MongoDatabase = connection[MongoDbDatabase]
    Collection=MongoDatabase[MongoDbCollection]
    
    return Collection


def CalculateSpecRatio(item):
    reference1 = 60000/item['ExecutionTimeBubble']
    reference2 = 60000/item['ExecutionTimePrimeNumberSearch']
    reference3 = 60000/item['ExecutionTimeFloatingPointOperation']
    
    perfList=[]
    perfList.append(reference1)
    perfList.append(reference2)
    perfList.append(reference3)
    
    return geo_mean(perfList)
    


def CalculateMultiCorePerf(item):
    reference = 600/item['ExecutionTimeParallelPrimeNumberSearch']
    
    return reference
    
    

@app.route('/')
def connectionCheck():
    return "Hello! you are now connected to CoolBenchmark's API."

    
        

@app.route('/postSpecs', methods=['POST'])    
def postSpecs():
    Connection          = ConnectionParameters()
    jsonResponse        = request.get_json(force=True)
    jsonResponse['SpecRatio']= CalculateSpecRatio(jsonResponse)
    jsonResponse['MultiCorePerf']=CalculateMultiCorePerf(jsonResponse)
    Connection.update({'MacAddress': jsonResponse['MacAddress']}, dict(jsonResponse), upsert=True)
    return 'success!'

             
@app.route('/getResults', methods=['GET'])
def getResults():
    args = request.args.to_dict()
    response=dict()
    Connection = ConnectionParameters()
    response['otherCluster']=[]
    response['selfResultNormal']=None
    rank=0
    if 'MacAddress' in args.keys():
        for item in Connection.find().sort("SpecRatio", pymongo.ASCENDING):
            if 'MacAddress' in item.keys():
                rank+=1
                if item['MacAddress']==args['MacAddress']:
                    response['selfResultNormal']= item['SpecRatio']
                    response['selfResultMulti']= item['MultiCorePerf']
                    response['rank']=rank
                else:
                    response['otherClusterNormal'].append(item['SpecRation'])
                    
    if response['selfResultNormal']==None:
        return 'Error! benchmark not found!'
    else:
        return jsonify(response)
    
    


@app.route('/getNew', methods=['GET'])
def getNew():
    args = request.args.to_dict()
    response=str()
    Connection = ConnectionParameters()
    rank=0
    if 'MacAddress' in args.keys():
        for item in Connection.find().sort("SpecRatio", pymongo.ASCENDING):
            if 'MacAddress' in item.keys():
                rank+=1
                if item['MacAddress']==args['MacAddress']:
                    response += str(item['SpecRatio'])+' '
                    response += str(item['MultiCorePerf'])+' '
                    response += str(rank)
                    
    if response==str():
        return 'Error! benchmark not found!'
    else:
        return response





if __name__ == '__main__':
    app.debug = True
    app.run(host="0.0.0.0",port=5010)



