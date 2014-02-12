(ns track-server.models.schema
  (:require [clojure.java.jdbc :as sql]
            [noir.io :as io]))

(def db-store "site.db")

(def db-spec {:classname "org.h2.Driver"
              :subprotocol "h2"
              :subname (str (io/resource-path) db-store)
              :user "sa"
              :password ""
              :make-pool? true
              :naming {:keys clojure.string/lower-case
                       :fields clojure.string/upper-case}})
(defn initialized?
  "checks to see if the database schema is present"
  []
  (.exists (new java.io.File (str (io/resource-path) db-store ".h2.db"))))


(defn create-devices-table []
  (sql/with-connection db-spec
    (sql/create-table
     :devices
     [:id "integer SERIAL PRIMARY KEY"]
     [:imei "varchar(30)"]
     [:created_at :timestamp]
     [:updated_at :timestamp])))


(defn create-registers-table []
  (sql/with-connection db-spec
    (sql/create-table
     :registers
     [:id "integer SERIAL PRIMARY KEY"]
     [:lat "varchar(30)"]
     [:lng "varchar(30)"]
     [:device_id :integer]
     [:created_at :timestamp]
     [:updated_at :timestamp])))

(defn create-tables
  "creates the database tables used by the application"
  []
  (create-devices-table)
  (create-registers-table))
