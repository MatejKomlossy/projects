using System.Collections;
using System.Collections.Generic;
using System.Linq;
using UnityEngine;

public class RoadSpawner : MonoBehaviour
{
    public List<GameObject> roads;

    private const float Offset = 59.99f;
    
    // Start is called before the first frame update
    void Start()
    {
        if (roads is { Count: > 0 })
        {
            roads = roads.OrderBy(r => r.transform.position.z).ToList();
        }
    }

    public void OnSpawnTriggerEntered()
    {
        var movedRoad = MoveAndClearRoad();
        SpawnObjectsOnRoad(movedRoad);
    }

    private GameObject MoveAndClearRoad()   //returns moved road
    {
        var movedRoad = roads[0];
        movedRoad.GetComponent<ObjectSpawner>().ClearObjects();
        roads.Remove(movedRoad);
        
        var newZ = roads[^1].transform.position.z + Offset;     //last road (first from end)
        movedRoad.transform.position = new Vector3(0, 0, newZ);
        
        roads.Add(movedRoad);
        return movedRoad;
    }
    
    private void SpawnObjectsOnRoad(GameObject road)
    {
        road.GetComponent<ObjectSpawner>().SpawnObjects();
    }
}
