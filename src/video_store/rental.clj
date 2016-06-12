(ns video-store.rental
  (:require [video-store.movie :as m]))

(defn make-rental [movie days-rented]
  {:title (:title movie)
   :amount (m/amount movie days-rented)
   :frequent-renter-points (m/frequent-renter-points movie days-rented)})