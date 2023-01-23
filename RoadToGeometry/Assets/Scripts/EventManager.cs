using System;
using Tasks;
using UnityEngine;

public class EventManager : MonoBehaviour
{
    public static EventManager Instance { get; private set; }

    public event Action<Task> TaskCompletedEvent;
    public event Action<GameObject> ObjectCollectedEvent;
    public event Action GameOverEvent; 

    private void Awake()    //Singleton
    {
        if (Instance == null)
        {
            Instance = this;
        }
        else
        {
            Destroy(gameObject);
        }
        DontDestroyOnLoad(gameObject);
    }

    public void TaskCompleted(Task task)
    {
        TaskCompletedEvent?.Invoke(task);
    }
    
    public void ObjectCollected(GameObject go)
    {
        ObjectCollectedEvent?.Invoke(go);
    }

    public void GameOver()
    {
        GameOverEvent?.Invoke();
    }
}