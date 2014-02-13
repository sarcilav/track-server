(ns track-server.routes.registers
  (:use compojure.core
        [taoensso.timbre :only [trace info warn debug error fatal]])
  (:require [track-server.views.layout :as layout]
            [track-server.util :as util]
            [track-server.models.db :as db]))

(defn cdebug [params]
  (info "*********\n" params  "\n*********\n"))

(defn create-register [imei lat lng]
  {:body (db/create-register imei lat lng)})

(defroutes registers-routes
  (POST "/registers.json" [imei lat lng] (create-register imei lat lng)))