# Open Food Facts

## Requirements
- Python 2.7
- `pip` v8.1.2
- Python packages
```
pip install -r requirements.txt
```

## Mongo db
Create the db
```
mkdir -pv mongodb/data
chmod 777 mongodb/data
```
Start the db
```
mongod --dbpath mongodb/data
```

## Run the server
```
make run
```
