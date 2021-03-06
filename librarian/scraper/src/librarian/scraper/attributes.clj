(ns librarian.scraper.attributes)

(def attributes {:tx/source {:db/doc "The datasource this transaction originates from."}
                 :tempid {:db/cardinality :db.cardinality/many
                          :db/unique :db.unique/identity
                          :librarian/internal true
                          :db/doc "Temporary bookkeeping property used by the scraper."}
                 :allow-incomplete {:db/index true
                                    :librarian/internal true
                                    :db/doc "Temporary bookkeeping property used by the scraper."}})
