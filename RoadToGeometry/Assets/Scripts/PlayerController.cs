using System;
using System.Collections;
using System.Collections.Generic;
using TMPro;
using UnityEngine;

public class PlayerController : MonoBehaviour
{
    public float forwardMovementSpeed = 10f;
    public SpawnManager spawnManager;

    public float playerMinX = 7.6f;
    public float playerMaxX = 11.3f;

    private float decelerationSpeed = 0.2f;

    // Update is called once per frame
    void Update()
    {

        if (transform.GetComponent<GameOver>().isGameOver) //slow down after Game Over
        {
            if (forwardMovementSpeed > 0.0f)
            {
                transform.position += Vector3.forward * (Time.deltaTime * forwardMovementSpeed);
                forwardMovementSpeed -= decelerationSpeed;
            }
        }

        transform.position += Vector3.forward * (Time.deltaTime * forwardMovementSpeed); //forward, comrades
        
        Quaternion headRotation = Camera.main.transform.rotation;
        Vector3 currentEulerAngles = headRotation.eulerAngles;

        if (currentEulerAngles.z > 0 && currentEulerAngles.z < 180)
        {
            if (transform.position.x > playerMinX)
            {
                float angle = Mathf.Min(currentEulerAngles.z, 90); // if more than 90, use 90 (max left speed)
                float sideMovementSpeed = forwardMovementSpeed * Mathf.Sin(Mathf.Deg2Rad * angle);
                transform.position += Vector3.left * (Time.deltaTime * sideMovementSpeed);
            }
        }
        else if (currentEulerAngles.z > 180 && currentEulerAngles.z < 360)
        {
            if (transform.position.x < playerMaxX)
            {
                float angle = Mathf.Max(currentEulerAngles.z, 270); // if more less than 270, use 270 (max right speed)
                float sideMovementSpeed = forwardMovementSpeed * Mathf.Sin(Mathf.Deg2Rad * angle);
                transform.position += Vector3.left * (Time.deltaTime * sideMovementSpeed);
            }
        }


        // PC
        if (Input.GetKey(KeyCode.W))
        {
            transform.position += Vector3.forward * (Time.deltaTime * forwardMovementSpeed);   
        }
        if (Input.GetKey(KeyCode.A))
        {
            if(transform.position.x > playerMinX)
                transform.position += Vector3.left * (Time.deltaTime * forwardMovementSpeed);    
        }
        if (Input.GetKey(KeyCode.D))
        {
            if(transform.position.x < playerMaxX)
                transform.position += Vector3.right * (Time.deltaTime * forwardMovementSpeed);
        }
    }

    private void OnTriggerEnter(Collider other)
    {
        if (other.CompareTag("SpawnTrigger"))
        {
            spawnManager.SpawnTriggerEntered();    
        }
    }
}
