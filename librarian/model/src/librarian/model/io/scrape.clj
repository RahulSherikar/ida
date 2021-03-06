(ns librarian.model.io.scrape
  (:require [clojure.java.io :as io]
            [clojure.tools.logging :as log]
            [clojure.spec.alpha :as s]
            [me.raynes.fs :as fs]
            [clojure.edn :as edn]
            [datascript.core :as d]
            [semver.core :as semver]
            [librarian.model.core :as m]
            [librarian.model.db :as mdb])
  (:import (java.util.zip GZIPInputStream
                          GZIPOutputStream)))

(def default-name "scrape.db")

(defn- get-file
  [file]
  (cond
    (fs/directory? file) (fs/file file default-name)
    (fs/file? file) (fs/file file)
    :else (throw (Error. "Invalid scrape path."))))

(defn write-scrape
  [file {:keys [name ecosystem meta]} scrape]
  (let [file (get-file file)
        out {:scrape scrape
             :name name
             :created (java.util.Date.)
             :ecosystem (:alias ecosystem)
             :ecosystem/version (:version ecosystem)
             :meta meta}]
    (log/info (str "Writing scrape to " file "..."))
    (io/make-parents file)
    (with-open [data (-> file
                         (io/output-stream)
                         (GZIPOutputStream.))]
      (io/copy (pr-str out) data))
    (log/info (str "Successfully wrote scrape to " file "."))
    file))

(defn read-scrape
  [file]
  (let [file (get-file file)
        scrape (with-open [data (-> file
                                    (io/input-stream)
                                    (GZIPInputStream.))]
                 (edn/read-string {:readers d/data-readers}
                                  (slurp data)))
        conformed (s/conform ::scrape-outer scrape)]
    (if (s/invalid? conformed)
      (throw (Exception. (str "Invalid scrape."
                              (s/explain-str ::scrape-outer scrape))))
      conformed)))

(defn query-scrape
  [scrape query & args]
  (let [{:keys [scrape ecosystem]} scrape]
    (apply mdb/q ecosystem query scrape args)))

(defn query-file
  [scrape-file query & args]
  (apply query-scrape (read-scrape scrape-file) query args))

(defn pull-scrape
  [scrape selector eid]
  (let [{:keys [scrape ecosystem]} scrape]
    (mdb/pull ecosystem scrape selector eid)))

(defn pull-file
  [scrape-file selector eid]
  (pull-scrape (read-scrape scrape-file) selector eid))

(defn- semver-compat
  [v1 v2]
  (= (-> v1 semver/parse :major)
     (-> v2 semver/parse :major)))

(s/def ::scrape-outer
       (s/and (s/keys :req-un [::scrape
                               ::name
                               ::created
                               ::ecosystem
                               ::meta])
              #(semver-compat (get-in % [:ecosystem :version])
                              (:ecosystem/version %))))

(s/def ::scrape d/db?)
(s/def ::name #(or (string? %) (symbol? %)))
(s/def ::created inst?)
(s/def ::ecosystem (s/and keyword?
                          #(contains? m/ecosystems %)
                          (s/conformer m/ecosystems)))
(s/def ::meta map?)
