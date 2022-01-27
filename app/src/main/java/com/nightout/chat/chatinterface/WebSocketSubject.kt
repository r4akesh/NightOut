package com.nightout.chat.chatinterface;

// This interface handles adding, deleting and updating
// all observers 
interface WebSocketSubject {
    fun register(o: WebSocketObserver)
    fun unregister(o: WebSocketObserver)
    fun notifyObserver(response: String)
}