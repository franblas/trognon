# Trognon backend
If you want to host your own backend instead of using the open food facts api, here is
an example of code written in python to reproduce the api.

## Requirements
- Python v2.7 + `pip` v8.1.2
- Python packages
```
pip install -r requirements.txt
```
- MongoDB

## Database
There is a nightly dump of the open food facts database in the MongoDB format. You can
have more informations here: http://world.openfoodfacts.org/data.

### Create the db
```
mkdir -pv mongodb/data
chmod 777 mongodb/data
wget http://world.openfoodfacts.org/data/openfoodfacts-mongodbdump.tar.gz
mongorestore openfoodfacts-mongodbdump.tar.gz
```

### Start the db
```
mongod --dbpath mongodb/data
```

## Run the server
```
make run
```

The api is then accessible `http://localhost:8002/api/v0/product/<barcode>.json`
