# -*- coding: utf-8 -*-

from flask import Flask, g
from flask_helpers import crossdomain, api_data, api_error
from db import db_connect, select_product_with_code

PORT = 8002
app = Flask(__name__)

@app.before_request
def connect():
    g.conn = db_connect()

@app.route('/ping')
@crossdomain(origin='*')
def ping():
    return 'pong'

@app.route('/api/v0/product/<code>', methods=['GET'])
@crossdomain(origin='*')
def get_product_from_code(code):
    code = code.replace('.json', '')
    data = select_product_with_code(g.conn, code)
    if not data: return api_error('Product with code {} not found...'.format(code))
    return api_data(data)

if __name__ == '__main__':
    app.run(host='0.0.0.0', port=PORT)
