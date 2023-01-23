using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.UI;

public class ValueManager : MonoBehaviour
{
    // Start is called before the first frame update
    void Start()
    {
        if (PlayerPrefs.GetInt(transform.gameObject.name) == 1)
        {
            transform.gameObject.GetComponent<Toggle>().isOn = false;
        } else
        {
            transform.gameObject.GetComponent<Toggle>().isOn = true;
        }
    }

    // Update is called once per frame
    void Update()
    {
        
    }
}
