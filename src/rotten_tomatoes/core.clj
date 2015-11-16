(ns rotten-tomatoes.core
  (:require [clojure.data.json :as json]
            [clj-http.client :as client]))

(def ^:dynamic *base-url* "http://api.rottentomatoes.com/api/public/v1.0")

(def ^:dynamic *movies-search-url* (str *base-url* "/movies.json"))

(defn- movie-cast-url [id]
  (str *base-url* "/movies/" id "/cast.json"))

(def ^:dynamic *movies-cast-url* (str *base-url*))

(defn- key->keyword [str]
  (keyword (clojure.string/replace str "_" "-")))

(defn- get-api
  ([api-key url]
   (get-api api-key url {}))
  ([api-key url query-params]
   (let [req (client/get url {:query-params (merge {"apiKey" api-key} query-params)})]
     (json/read-str (:body req) :key-fn key->keyword))))

(defn search-movies [api-key query]
  (get-api api-key *movies-search-url* {"q" query}))

(defn movie-cast [api-key id]
  (get-api api-key (movie-cast-url id)))
