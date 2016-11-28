# -*- coding: utf-8 -*-

import pymongo
import time

MONGO_HOST = 'localhost'
MONGO_PORT = 27017
MONGO_DATABASE = 'off'
MONGO_COLLECTION = 'products'

def db_connect():
    max_attempts, n_attempts = 5, 0
    while n_attempts < max_attempts:
        try:
            return pymongo.MongoClient(host=MONGO_HOST, port=MONGO_PORT)
        except pymongo.errors.ConnectionFailure, e:
            n_attempts += 1
            print 'Attempt:\t%d\nERROR:\t%s' % (n_attempts, e)
            time.sleep(2)
        if n_attempts == max_attempts:
            raise Exception("Could not connect to mongo database {}:{}".format(MONGO_HOST, MONGO_PORT))

def select_product_with_code(conn, code):
    if not code: return None
    product = None
    try:
        products_with_code = conn[MONGO_DATABASE][MONGO_COLLECTION].find({
            '$and': [
                {'code': {'$exists': True}},
                {'code': str(code)},
            ],
            'product_name': {'$ne': ""}
        })
        product = [p for p in products_with_code][0]
    except Exception as e:
        print e
    return product
