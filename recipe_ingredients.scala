## Imports
import org.apache.spark.sql.SQLContext

## stop spark context created automatically
spark.stop

## Set configuration parameters
val spark = org.apache.spark.sql.SparkSession.builder.master("local").appName("Load Recipes into Database").getOrCreate;

import spark.implicits._

## // Use first line of all files as header
## // Automatically infer data types
val recipeEntries = spark.read.format("csv").option("header", "true").option("inferSchema", "true").load("/tmp/recipes")

## Since we only need to add new recipe ID if it does not exist, we use ifNotExists
## selected data is then added to recipe table
val writeConf = WriteConf(ifNotExists = true)
val recipeData = recipeEntries.select("recipe_id","recipe_name","description")
recipeData.write.format("com.databricks.spark.csv").option("header", "true").options(Map( "table" -> "Recipe", "keyspace" -> "recipe", "WriteConf" -> writeConf)).save()

## selected data is added to IngredientUpdate Table to conform to Data Model
val ingredientUpdate = recipeEntries.select("recipe_id","ingredient_name","active","updated_date","created_date")
ingredientUpdate.write.format("org.apache.spark.sql.cassandra").mode(Overwrite).options(Map( "table" -> "IngredientUpdate", "keyspace" -> "recipe")).save()

spark.stop