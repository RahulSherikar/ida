(ns lib-scraper.model.concepts.function
  (:require [clojure.spec.alpha :as s]
            [lib-scraper.helpers.spec :as hs]))

(def concept {::name {:db/doc "Name of the function."}
              ::param {:db/valueType :db.type/ref
                       :db/cardinality :db.cardinality/many
                       :db/isComponent true
                       :db/doc "A parameter of the function."}})

(s/def ::name string?)
(s/def ::concept (hs/entity-keys :req [::name]))