## Ingest Recipe Data

## Assumptions while interpreting the data (in recipes.csv)
1. Created Date is the date when CSV was generated from legacy system
2. Updated Date is the date of updating the ingredient in the system
3. Cassandra version is 3.0 or above
4. The CSV file is ingested into Hadoop at a location /tmp/recipes

## Data Model - Cassandra Database to be used as the persistence system
1. Two tables to be created - Recipe and Ingredient_Update
2. Recipe Table will have columns --> recipe_id, recipe_name, description
3. Ingredient_Update will have columns --> recipe_id, ingredient_name, active, updated_date (alias recipe_updated_date), created_date (alias data_extraction_date)
4. Column recipe_id of Recipe Table is the primary key and column recipe_id of Ingredient_Update Table is the foreign key
5. Had it been a relational model, we could have created a separate ingredient table as well. However, in a cassandra database, this will be an overkill since recipes for individual ingredients also need to be selected.

## Contents of the repository
1. recipes.csv - This contains the test data to be ingested from CSV
2. recipe_ingredients.scala - This contains scala code to ingest the CSV file into Spark and then add it to Cassandra tables
3. cql_queries - This contains the cql queries to select data from Cassandra tables
