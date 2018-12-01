(ns lib-scraper.model.concepts.core
  (:require [lib-scraper.model.concepts.common :as common]
            [lib-scraper.model.concepts.package :as package]
            [lib-scraper.model.concepts.class :as class]
            [lib-scraper.model.concepts.function :as function]
            [lib-scraper.model.concepts.parameter :as parameter]))

(def concepts (merge common/spec
                     package/spec
                     class/spec
                     function/spec
                     parameter/spec))