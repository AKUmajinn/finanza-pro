package com.example.finanzapro.adapter

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.finanzapro.model.Transaction
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.util.UUID

class TransactionViewModel: ViewModel() {

    private val db = Firebase.firestore

    private var _listTransactions = MutableLiveData<List<Transaction>>(emptyList())
    val listTransactions: LiveData<List<Transaction>> = _listTransactions

    init {
        getTransactions()
    }

    fun getTransactions() {
        viewModelScope.launch (Dispatchers.IO) {
            try {
                var result = db.collection("transactions").get().await()
                val transactions = result.documents.mapNotNull { it.toObject(Transaction::class.java) }
                _listTransactions.postValue(transactions)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun saveTransactions(transaction: Transaction) {
        transaction.id = UUID.randomUUID().toString()
        viewModelScope.launch(Dispatchers.IO) {
            try {
                db.collection("transactions").document(transaction.id).set(transaction).await()
                _listTransactions.postValue(_listTransactions.value?.plus(transaction))
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun updateTransaction(transaction: Transaction) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                db.collection("transactions").document(transaction.id).update(transaction.toMap())
                    .await()
                _listTransactions.postValue(_listTransactions.value?.map {
                    if (it.id == transaction.id) transaction else it
                })
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun deleteTransaction(id: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                db.collection("transactions").document(id).delete().await()
                _listTransactions.postValue(_listTransactions.value?.filter { it.id != id })
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

}