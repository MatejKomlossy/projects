using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class SpawnManager : MonoBehaviour
{
    private RoadSpawner _roadSpawner;
    // Start is called before the first frame update
    void Start()
    {
        _roadSpawner = GetComponent<RoadSpawner>();
    }

    // Update is called once per frame
    void Update()
    {
        
    }

    public void SpawnTriggerEntered()
    {
        _roadSpawner.OnSpawnTriggerEntered();
    }
}
