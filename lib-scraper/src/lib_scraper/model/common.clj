(ns lib-scraper.model.common)

(def attributes {:description {:db/cardinality :db.cardinality/many
                               :db/doc "Docstring for the concept."}
                 :type {:db/cardinality :db.cardinality/many
                        :db/doc "Type of the concept."}})