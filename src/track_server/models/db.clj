(ns track-server.models.db
  (:use korma.core
        [korma.db :only (defdb)])
  (:require [track-server.models.schema :as schema]))

(defdb db schema/db-spec)

;; workaround to handle korma return from inserts
(defn get-row [type result]
  (let [id (second (first result))]
    (first (select type (where {:id id})))))

(declare device get-or-create-device-by-imei)

(defentity registers
  (belongs-to device))

(defn create-register [imei lat lng]
  (get-row registers
           (let [dev (get-or-create-device-by-imei imei)]
             (insert registers
                     (values {:device_id (:id dev)
                              :lat lat
                              :lng lng})))))

(defentity devices
  (has-many registers))

(defn create-device [imei]
  (get-row devices
           (insert devices
                   (values {:imei imei}))))

(defn get-device-by-imei [imei]
  (first (select devices
                 (where {:imei imei})
                 (limit 1))))

(defn get-or-create-device-by-imei [imei]
  (let [dev (get-device-by-imei imei)]
    (if (nil? dev)
      (create-device imei)
      dev)))