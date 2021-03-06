(ns librarian.model.common)

(def attributes {:description {:db/cardinality :db.cardinality/many
                               :db/doc "Description of the concept."}
                 :description-summary {:db/doc "Short summarized description of the concept."}
                 :type {:db/index true
                        :db/cardinality :db.cardinality/many
                        :db/doc "Type of the concept."}})
